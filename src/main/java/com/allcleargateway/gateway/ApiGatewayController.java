package com.allcleargateway.gateway;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class ApiGatewayController {

    private final GatewayClient gatewayClient;

    @Autowired
    public ApiGatewayController(GatewayClient gatewayClient) {
        this.gatewayClient = gatewayClient;
    }

    @RequestMapping(value = "/waiting/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardWaitingRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request);
    }

    @RequestMapping(value = "/user/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request);
    }

    @RequestMapping(value = "/lecture/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardLectureRequest(HttpServletRequest request) throws IOException {
        log.info("여기");
        return forwardRequest(request);
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request) throws IOException {
        // 요청 URI에서 상대 경로를 추출
        String requestURI = request.getRequestURI();
        String relativeUrl = requestURI.substring(requestURI.indexOf('/', 1) + 1);

        // 요청의 본문(body) 읽기
        String body = request.getReader().lines().collect(Collectors.joining("\n"));

        // 요청 헤더 읽기
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        headerName -> String.join(",", Collections.list(request.getHeaders(headerName)))
                ));

        // 외부 API로 요청 전달
        return gatewayClient.forwardRequest(headers, body, relativeUrl);
    }
}
