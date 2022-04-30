package com.vau.app.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

import static org.springframework.http.MediaType.TEXT_PLAIN;

/**
 * Web service client
 */
@Configuration
public class WebClientConfig {

    private static final int TIME_OUT = 15000;

    @Bean(name = "service_client")
    public WebClient getServiceClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).codecs(configure ->
                                configure
                                        .defaultCodecs()
                                        // 10 MB
                                        .maxInMemorySize(10 * 1024 * 1024)
                        ).build()
                )
                .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                .build();
    }

    private HttpClient getHttpClient() {
        return HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIME_OUT)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(TIME_OUT, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIME_OUT, TimeUnit.MILLISECONDS));
                });
    }

    private UriBuilderFactory defaultUriBuilderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
        return factory;
    }

    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().encoder(new Jackson2JsonEncoder(new ObjectMapper(), TEXT_PLAIN));
        clientCodecConfigurer.customCodecs().decoder(new Jackson2JsonDecoder(new ObjectMapper(), TEXT_PLAIN));
    }
}
