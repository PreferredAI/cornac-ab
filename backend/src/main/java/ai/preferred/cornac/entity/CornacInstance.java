package ai.preferred.cornac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CornacInstance {
    private String serviceName;
    private int port;
    private Process process;
    private WebClient webClient;

}
