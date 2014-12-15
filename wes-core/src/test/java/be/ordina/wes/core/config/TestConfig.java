package be.ordina.wes.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import be.ordina.wes.core.service.ClientFactory;
import be.ordina.wes.core.service.NodeClientFactory;

@Configuration
@ComponentScan(basePackages = "be.ordina.wes.core")
public class TestConfig {

    @Bean(initMethod = "getInstance", destroyMethod = "destroyInstance")
    public ClientFactory elasticsearchClientFactory() {
    	return new NodeClientFactory();
    }
    
}
