package ai.preferred.cornac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CornacInstanceDto {
    private String serviceName;
    private int port;
    private String status;
}
