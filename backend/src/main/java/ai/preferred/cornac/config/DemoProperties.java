package ai.preferred.cornac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "demo")
public class DemoProperties {
    private String userCollection = "users";
    private String itemCollection = "items";
}
