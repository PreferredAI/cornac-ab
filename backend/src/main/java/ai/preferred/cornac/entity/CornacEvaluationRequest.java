package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CornacEvaluationRequest {
    private List<String> metrics; // e.g. ["RMSE()", "NDCG(k=10)"]
    private List<List<Object>> data; // e.g. [["123", "1539", 1], ["123", "2", 1], ["124", "1", 1]]

    @Override
    public String toString() {
        return "CornacEvaluationRequest{" +
                "metrics=" + metrics +
                ", data=" + data +
                '}';
    }
}
