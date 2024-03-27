package ai.preferred.cornac.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoConfig {
    @Autowired
    private DemoProperties demoProperties;

    @Bean
    public String getUserCollection(){
        return demoProperties.getUserCollection();
    }

    @Bean
    public String getItemCollection(){
        return demoProperties.getItemCollection();
    }
}
