package ai.preferred.cornac.service;

import ai.preferred.cornac.entity.DemoItem;
import ai.preferred.cornac.entity.DemoUser;
import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.entity.Recommendation;
import ai.preferred.cornac.repository.DemoItemRepository;
import ai.preferred.cornac.repository.DemoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class DemoService {

    @Autowired
    private DemoUserRepository demoUserRepository;

    @Autowired
    private DemoItemRepository demoItemRepository;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private FeedbackService feedbackService;

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

    public DemoItem createItem(String itemId, Map<String, String> attributes){
        DemoItem item = demoItemRepository.findById(itemId).orElse(null);

        if (item != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Item already exists");
        }

        item = new DemoItem(itemId, attributes);
        return demoItemRepository.save(item);
    }

    public DemoItem getItem(String itemId){
        return demoItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public void deleteItem(String itemId){
        demoItemRepository.deleteById(itemId);
    }

    public DemoItem updateItem(String itemId, HashMap<String, String> attributes){
        DemoItem item = demoItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setAttributes(attributes);
        return demoItemRepository.save(item);
    }

    public List<DemoItem> getRecommendations(String userId){
        // We simulate that we are on a separate service.
        // Call the recommend service
        List<String> recommendations = recommendService.getRecommendations(userId, "30").getRecommendations();

        // Get the items from the database, these are unordered
        List<DemoItem> demoItemList = demoItemRepository.findAllByItemIdIn(recommendations);
        Map<String, DemoItem> demoItemMap = new HashMap<>();
        demoItemList.forEach(demoItem -> demoItemMap.put(demoItem.getItemId(), demoItem));

        // Retrieve them in order and return them
        return recommendations.stream().map(demoItemMap::get).toList();
    }

    public void submitFeedback(String recommendId, String itemId, String feedback){
        // We simulate that we are on a separate service
        // Call the feedback service
        feedbackService.addFeedback(recommendId, itemId, Integer.parseInt(feedback));
    }
}
