package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "recommend_log")
public class RecommendLog {
    @Id
    private String id;
    private Integer experimentId;

    private List<String> recommendations;
    private RecommendationQuery query;
    private List<Feedback> feedbacks;

}
