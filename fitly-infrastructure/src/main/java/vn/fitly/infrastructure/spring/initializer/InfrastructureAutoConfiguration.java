package vn.fitly.infrastructure.spring.initializer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "vn.fitly.infrastructure")
@EnableConfigurationProperties(FitlyDatasourceConfig.class)
public class InfrastructureAutoConfiguration {
}