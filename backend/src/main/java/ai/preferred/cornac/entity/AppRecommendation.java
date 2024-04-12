package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppRecommendation {
    private String recommendationId;
    private List<AppItem> pastRatings;
    private List<AppItem> items;
}
