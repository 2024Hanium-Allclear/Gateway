package com.allcleargateway.gateway.config;

import feign.Client;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class FeignConfig {

    @Value("${server.ssl.key-store-password}")
    private String keyStorePassword;

    @Bean
    public Client feignClient() {
        return new Client.Default(
                getSSLSocketFactory(),
                javax.net.ssl.HttpsURLConnection.getDefaultHostnameVerifier()
        );
    }

    @Bean
    public RequestInterceptor dynamicUrlRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                String path = template.url();
                // 동적 URL 변경 로직 추가
                if (path.startsWith("/waiting")) {
                    template.target("https://waiting.allclear-server.com:8081");
                } else if (path.startsWith("/user")) {
                    template.target("https://user.allclear-server.com:8082");
                } else if (path.startsWith("/lecture")) {
                    template.target("https://lecture.allclear-server.com:8083");
                }
            }
        };
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (InputStream keyStoreStream = getClass().getResourceAsStream("hanium.jks")) {
                keyStore.load(keyStoreStream, keyStorePassword.toCharArray());
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL socket factory", e);
        }
    }
}
