package com.teach.predict.MLAPIs;

import com.teach.predict.config.MLServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class MLClientAPIs {

    @Autowired
    @Qualifier(value = "loadBalancedRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private MLServiceConfig mlServiceConfig;

    public ResponseEntity performMLApiRequest(String url, Object requestBody, HttpMethod method, Class clazz) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        headers.setAll(map);
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(url, method, request, clazz, headers);
    }

    public String getMLFullUrl(String path) {
        return mlServiceConfig.getMlServiceUrl() + path;
    }

}
