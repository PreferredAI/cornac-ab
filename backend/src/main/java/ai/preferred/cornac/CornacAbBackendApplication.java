package ai.preferred.cornac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@ConfigurationPropertiesScan
@SpringBootApplication(exclude = {ElasticsearchDataAutoConfiguration.class})
public class CornacAbBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CornacAbBackendApplication.class, args);
	}

}
