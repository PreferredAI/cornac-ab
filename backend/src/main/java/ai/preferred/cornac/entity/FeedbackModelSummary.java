package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackModelSummary {
    private String model;
    private long feedbackCount;
    private long uniqueUsers;
    private long uniqueItems;
}

