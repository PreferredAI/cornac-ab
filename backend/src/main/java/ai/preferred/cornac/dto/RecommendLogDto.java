package ai.preferred.cornac.dto;

import ai.preferred.cornac.entity.Feedback;
import ai.preferred.cornac.entity.RecommendationQuery;
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
@Document(indexName = "recommendLog")
public class RecommendLogDto {
    @Id
    private String id;

    private List<String> recommendations;
    private RecommendationQuery query;

}
