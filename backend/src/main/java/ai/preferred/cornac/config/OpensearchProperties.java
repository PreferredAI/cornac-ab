package ai.preferred.cornac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "opensearch")
@Getter
@Setter
public class OpensearchProperties {
    private String host = "localhost";
    private int port = 9200;
}
