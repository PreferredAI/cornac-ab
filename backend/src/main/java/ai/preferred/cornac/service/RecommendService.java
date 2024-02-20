package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.RecommendLogRepository;
import ai.preferred.cornac.repository.UserAbAllocationRepository;
import jakarta.annotation.PreDestroy;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.Random;

@Service
public class RecommendService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RecommendService.class);

    @Autowired
    private RecommendLogRepository recommendLogRepository;

    @Autowired
    private UserAbAllocationRepository userAbAllocationRepository;

    @Autowired
    private ExperimentService experimentService;

    @Autowired
    private CornacService cornacService;

    @Autowired
    private ModelMapper modelMapper;

    private Integer getAbInstanceGroup(Experiment experiment, String userId){
        Integer experimentId = experiment.getId();

        int numInstances = cornacService.getCornacInstancesForExperiment(experimentId).size();

        UserAbAllocation userAbAllocation = userAbAllocationRepository.findByExperimentIdAndUserId(experimentId, userId);

        if (userAbAllocation == null) {
            // allocate an ab-testing group
            // - abGroup is a random number between 0 and numInstances
            // - this is a deterministic random operation.
            // - This will give the same instance on the same seed for the same user.
            Random rand = new Random(experiment.getUserSeed());
            int randInt = rand.nextInt(userId.hashCode());
            int abGroup = randInt % numInstances;
            userAbAllocationRepository.save(new UserAbAllocation(null, userId, abGroup, experiment));
            return abGroup;
        }

        return userAbAllocation.getAbGroup();
    }

    public RecommendLogDto getRecommendations(String userId, String k){
        Experiment experiment = experimentService.getCurrentExperiment();

        if (experiment == null){
            throw new ErrorResponseException(HttpStatus.BAD_REQUEST,
                    new RuntimeException("No experiment is currently running.")
            );
        }

        Integer chosenInstance = getAbInstanceGroup(experiment, userId);

        CornacInstance cornacInstance = cornacService.getCornacInstancesForExperiment(experiment.getId()).get(chosenInstance);
        WebClient webClient = cornacInstance.getWebClient();
        Recommendation recommendation = getApiRecommendation(webClient, userId, k);

        // add recommendation to log
        RecommendLog log = new RecommendLog(
                null,
                experiment.getId(),
                userId,
                DateTime.now(),
                recommendation.getRecommendations()
        );

        RecommendLog recommendLog = recommendLogRepository.save(log);
        return convertToRecommendLogDto(recommendLog);
    }

    public Recommendation getApiRecommendation(WebClient webClient, String userId, String k) {


        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                    .path("recommend")
                    .queryParam("uid", userId)
                    .queryParamIfPresent("k", Optional.ofNullable(k))
                    .build())
                .retrieve()
                .bodyToMono(Recommendation.class)
                .block();
    }

    private RecommendLogDto convertToRecommendLogDto(RecommendLog recommendLog) {
        return modelMapper.map(recommendLog, RecommendLogDto.class);
    }



    @PreDestroy
    public void destroy(){
        System.out.println("destroy called, closing all cornac instances.");
        cornacService.getCurrentActiveCornacInstancesForExperiment().forEach(cornacInstance -> cornacInstance.getProcess().destroy());
    }


}
