package com.teach.predict.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("ml-service-config")
public class MLServiceConfig {
    private String mlServiceUrl;

    public String getMlServiceUrl() {
        return mlServiceUrl;
    }

    public void setMlServiceUrl(String mlServiceUrl) {
        this.mlServiceUrl = mlServiceUrl;
    }
}
