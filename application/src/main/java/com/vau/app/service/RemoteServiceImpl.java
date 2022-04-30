package com.vau.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class RemoteServiceImpl implements IRemoteService {

    private ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<>(){};

    @Autowired
    @Qualifier("service_client")
    private WebClient webClient;

    @Override
    public Object GET(String url, Map<String, Object> params, Map<String, Object> headers) {
         return webClient.get()
                .uri(url, uriBuilder -> {
                    Set<Map.Entry<String, Object>> entrySet = params.entrySet();
                    entrySet.forEach(entry -> {
                        uriBuilder.queryParam(entry.getKey(), entry.getValue());
                    });
                    return uriBuilder.build();
                })
                .headers(httpHeaders -> {
                    Set<Map.Entry<String, Object>> entrySet = headers.entrySet();
                    entrySet.forEach(entry -> {
                        httpHeaders.add(entry.getKey(), entry.getValue().toString());
                    });
                })
                .retrieve()
                .bodyToMono(parameterizedTypeReference)
                .block();
    }
}
