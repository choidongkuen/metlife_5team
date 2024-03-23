package com.metlife.core.config;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

@Component
@EnableJpaAuditing
@ConfigurationPropertiesScan("com.metlife.core.property")
public class AppConfig {
}
