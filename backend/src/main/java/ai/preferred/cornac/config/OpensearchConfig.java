package ai.preferred.cornac.config;

import org.opensearch.client.RestHighLevelClient;
import org.opensearch.data.client.orhlc.AbstractOpenSearchConfiguration;
import org.opensearch.data.client.orhlc.ClientConfiguration;
import org.opensearch.data.client.orhlc.RestClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpensearchConfig extends AbstractOpenSearchConfiguration {

    @Autowired
    private OpensearchProperties opensearchProperties;

    @Override
    @Bean
    public RestHighLevelClient opensearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(opensearchProperties.getHost() + ":" + opensearchProperties.getPort())
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
