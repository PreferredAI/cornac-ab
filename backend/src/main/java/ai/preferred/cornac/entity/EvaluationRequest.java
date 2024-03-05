package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EvaluationRequest {
    private List<MetricRequest> metrics;
    private List<Feedback> data;
    private String experimentId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    @Override
    public String toString() {
        return "EvaluationRequest{" +
                "metrics=" + metrics +
                ", data=" + data +
                ", experimentId='" + experimentId + '\'' +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }
}
