package ai.preferred.cornac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ConfigurationPropertiesScan
@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
@EnableScheduling
public class CornacAbBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CornacAbBackendApplication.class, args);
	}

}
