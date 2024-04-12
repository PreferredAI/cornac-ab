package ai.preferred.cornac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAbAllocationDto {

    private String userId;
    private String abGroup;
    private Integer recommendCount;
    private Integer feedbackCount;
    private LocalDateTime lastTimestamp;

}
