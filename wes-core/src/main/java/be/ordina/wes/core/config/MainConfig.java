package be.ordina.wes.core.config;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import be.ordina.wes.core.service.ClientFactory;
import be.ordina.wes.core.service.ClientFactoryImpl;

@Configuration
public class MainConfig {

    @Bean
    public Client elasticsearchClient() {
    	return elasticsearchClientFactory().getInstance();
    }
    
    @Bean(initMethod = "getInstance", destroyMethod = "destroyInstance")
    public ClientFactory elasticsearchClientFactory() {
    	return new ClientFactoryImpl();
    }
    
}
