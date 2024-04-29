package ai.preferred.cornac.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "cornac")
public class CornacProperties {
    private String executableDir = "cornac";
    private String uploadDir = "uploads";
}
