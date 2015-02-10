package be.ordina.wes.exercises.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import be.ordina.wes.core.config.MainConfig;
import be.ordina.wes.core.service.ClientFactory;

@Configuration
@ComponentScan(basePackageClasses = { MainConfig.class, ClientFactory.class })
public class TestConfig {

}
