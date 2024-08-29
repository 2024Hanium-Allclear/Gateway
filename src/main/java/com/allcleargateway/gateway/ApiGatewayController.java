package com.allcleargateway.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiGatewayController {

    private final WaitingClient waitingClient;
    private final UserClient userClient;
    private final LectureClient lectureClient;

    @Autowired
    public ApiGatewayController(WaitingClient waitingClient, UserClient userClient, LectureClient lectureClient) {
        this.waitingClient = waitingClient;
        this.userClient = userClient;
        this.lectureClient = lectureClient;
    }

    @RequestMapping(value = "/waiting/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardWaitingRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "waiting");
    }

    @RequestMapping(value = "/user/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "user");
    }

    @RequestMapping(value = "/lecture/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardLectureRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "lecture");
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, String endpoint) throws IOException {
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
        ResponseEntity<String> response;
        switch (endpoint) {
            case "waiting":
                response = waitingClient.forwardRequest(headers, body, relativeUrl);
                break;
            case "user":
                response = userClient.forwardRequest(headers, body, relativeUrl);
                break;
            case "lecture":
                response = lectureClient.forwardRequest(headers, body, relativeUrl);
                break;
            default:
                throw new IllegalArgumentException("Unknown endpoint: " + endpoint);
        }

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}
