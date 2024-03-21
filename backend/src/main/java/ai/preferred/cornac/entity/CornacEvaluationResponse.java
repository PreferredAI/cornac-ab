package ai.preferred.cornac.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
public class CornacEvaluationResponse {
    private Map<String, Double> result; // key: metric, value: value (e.g. "precision@5": 0.5)
    @JsonProperty("user_result")
    private Map<String, Map<String, Double>> userResult; // key: metric, value: result (e.g. "precision@5": {"123": 0.5, "124": 0.3})

    @Override
    public String toString() {
        return "CornacEvaluationResponse{" +
                "result=" + result +
                ", userResult=" + userResult +
                '}';
    }
}
