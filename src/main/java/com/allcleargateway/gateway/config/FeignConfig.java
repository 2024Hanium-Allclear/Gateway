package com.allcleargateway.gateway.config;

import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    private final SSLConfig sslConfig;

    public FeignConfig(SSLConfig sslConfig) {
        this.sslConfig = sslConfig;
    }

    @Bean
    public Client feignClient() throws Exception {
        return new Client.Default(
                sslConfig.sslContext().getSocketFactory(),
                javax.net.ssl.HttpsURLConnection.getDefaultHostnameVerifier()
        );
    }
}
