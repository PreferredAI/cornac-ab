package ai.preferred.cornac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDto {
    private List<String> recommendations;
    private Query query;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Query {
    private String uid;
    private String k;
    @Value("remove_seen")
    private Boolean removeSeen;
}
