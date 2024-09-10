package com.allcleargateway.gateway.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class SSLConfig {

    @Value("${server.ssl.key-store-password}")
    private String password;

    @Bean
    public SSLContext sslContext() throws Exception {
        // KeyStore 로드
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream keyStoreInput = new FileInputStream("keystore.p12")) {
            keyStore.load(keyStoreInput, password.toCharArray());
        }

        // TrustStore 로드
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream trustStoreInput = new FileInputStream("hanium.jks")) {
            trustStore.load(trustStoreInput, password.toCharArray());
        }

        // KeyManagerFactory와 TrustManagerFactory 설정
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, password.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // SSLContext 생성 및 초기화
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return sslContext;
    }
}
