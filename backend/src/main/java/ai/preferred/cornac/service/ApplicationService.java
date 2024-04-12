package ai.preferred.cornac.service;

import ai.preferred.cornac.dto.RecommendLogDto;
import ai.preferred.cornac.entity.*;
import ai.preferred.cornac.repository.DemoItemRepository;
import ai.preferred.cornac.repository.DemoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ApplicationService {

    @Autowired
    private DemoUserRepository demoUserRepository;

    @Autowired
    private DemoItemRepository demoItemRepository;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ExperimentService experimentService;

    public DemoUser createUser(String userId, Map<String, String> attributes){
        DemoUser user = demoUserRepository.findById(userId).orElse(null);

        if (user != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        user = new DemoUser(userId, attributes);
        return demoUserRepository.save(user);
    }

    public DemoUser getUser(String userId){
        return demoUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<DemoUser> getUsers(){
        List<DemoUser> users = new ArrayList<>();
        try (Stream<DemoUser> userStream = demoUserRepository.findAllBy()){
            users.addAll(userStream.toList());
        }
        return users;
    }

    public void deleteUser(String userId){
        demoUserRepository.deleteById(userId);
    }

    public DemoUser updateUser(String userId, Map<String, String> attributes){
        DemoUser user = demoUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setAttributes(attributes);
        return demoUserRepository.save(user);
    }

    public AppItem createItem(String itemId, Map<String, Object> attributes){
        AppItem item = demoItemRepository.findById(itemId).orElse(null);

        if (item != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already exists");
        }

        item = new AppItem(itemId, attributes);
        return demoItemRepository.save(item);
    }

    public AppItem getItem(String itemId){
        return demoItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteItem(String itemId){
        demoItemRepository.deleteById(itemId);
    }

    public AppItem updateItem(String itemId, HashMap<String, Object> attributes){
        AppItem item = demoItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setAttributes(attributes);
        return demoItemRepository.save(item);
    }

    public AppRecommendation getRecommendations(String userId){
        // We simulate that we are on a separate service.
        // Call the recommend service
        RecommendLogDto recommendLog = recommendService.getRecommendations(userId, "30");
        String recommendId = recommendLog.getId();
        List<String> recommendations = recommendLog.getRecommendations();

        // Call the feedback service and get past ratings
        List<Feedback> pastRatings = experimentService.getPastRatings(userId);
        List<String> pastRatingsItemIds = pastRatings.stream().map(Feedback::getItemId).toList();

        // Get the items from the database, these are unordered
        List<String> allItemIds = new ArrayList<>();
        allItemIds.addAll(recommendations);
        allItemIds.addAll(pastRatingsItemIds);

        List<AppItem> appItemList = demoItemRepository.findAllByItemIdIn(allItemIds);
        Map<String, AppItem> demoItemMap = new HashMap<>();
        appItemList.forEach(appItem -> demoItemMap.put(appItem.getItemId(), appItem));

        // Retrieve them in order and return them
        List<AppItem> itemList = recommendations.stream().map(demoItemMap::get).toList();
        List<AppItem> ratedList = pastRatings.stream().map(pastRating -> {
            AppItem appItem = demoItemMap.get(pastRating.getItemId());
            appItem.getAttributes().put("userRating", pastRating.getRating());
            return appItem;
        }).toList();

        return new AppRecommendation(recommendId, ratedList, itemList);
    }

    public Feedback postFeedback(String recommendId, String itemId, Integer rating, String action){
        // We simulate that we are on a separate service
        // Call the feedback service
        return feedbackService.addFeedback(recommendId, itemId, rating, action);
    }
}
