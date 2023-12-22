package ai.preferred.cornac.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    private String userId;
    private String itemId;
    private Integer rating;
    private Date timestamp;
}
