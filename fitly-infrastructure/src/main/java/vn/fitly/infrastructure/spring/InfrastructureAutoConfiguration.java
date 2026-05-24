package vn.fitly.infrastructure.spring;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "vn.fitly.infrastructure")
@EnableConfigurationProperties({ FitlyDatasourceConfig.class, FitlySessionConfig.class })
public class InfrastructureAutoConfiguration {
}
