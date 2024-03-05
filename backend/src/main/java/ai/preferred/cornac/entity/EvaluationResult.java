package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResult {
    private List<String> metrics;
    private List<MetricResult> modelMetricResults;
    private List<TResult> tResults;
}
