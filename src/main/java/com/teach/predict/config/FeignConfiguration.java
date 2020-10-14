package com.teach.predict.config;

import com.teach.predict.MLAPIs.MLErrorDecoder;
import feign.codec.ErrorDecoder;
import feign.form.FormEncoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableFeignClients(basePackages = "com.teach.predict")
public class FeignConfiguration {

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.BASIC;
    }

    @Autowired
    protected ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    @Primary
    FormEncoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    ErrorDecoder feignErrorDecoder() {
        return new MLErrorDecoder();
    }
}
