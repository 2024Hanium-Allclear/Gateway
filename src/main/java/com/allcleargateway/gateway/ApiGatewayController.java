package com.allcleargateway.gateway;

import com.allcleargateway.gateway.global.client.LectureClient;
import com.allcleargateway.gateway.global.client.UserClient;
import com.allcleargateway.gateway.global.client.WaitingClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/waiting/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> forwardWaitingRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "waiting");
    }

    @RequestMapping(value = "/user/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "user");
    }

    @RequestMapping(value = "/lecture/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<String> forwardLectureRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "lecture");
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, String endpoint) throws IOException {
        // 요청 URI에서 상대 경로를 추출합니다.
        String requestURI = request.getRequestURI();
        String relativeUrl = requestURI.substring(requestURI.indexOf('/', 1) + 1);

        // 요청의 본문(body)을 읽어옵니다.
        String body = request.getReader().lines().collect(Collectors.joining("\n"));

        // 요청 헤더를 읽어옵니다.
        Map<String, String> headers = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        headerName -> String.join(",", Collections.list(request.getHeaders(headerName)))
                ));

        // 외부 API로 요청을 전달합니다.
        ResponseEntity<String> response;
        switch (endpoint) {
            case "waiting":
                response = forwardToWaitingClient(request.getMethod(), headers, body, relativeUrl);
                break;
            case "user":
                response = forwardToUserClient(request.getMethod(), headers, body, relativeUrl);
                break;
            case "lecture":
                response = forwardToLectureClient(request.getMethod(), headers, body, relativeUrl);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 엔드포인트입니다: " + endpoint);
        }

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }

    private ResponseEntity<String> forwardToWaitingClient(String method, Map<String, String> headers, String body, String relativeUrl) {
        switch (method) {
            case "GET":
                return waitingClient.forwardGetRequest(headers, relativeUrl);
            case "POST":
                return waitingClient.forwardPostRequest(headers, body, relativeUrl);
            case "PUT":
                return waitingClient.forwardPutRequest(headers, body, relativeUrl);
            case "DELETE":
                return waitingClient.forwardDeleteRequest(headers, body, relativeUrl);
            case "PATCH":
                return waitingClient.forwardPatchRequest(headers, body, relativeUrl);
            default:
                throw new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다: " + method);
        }
    }

    private ResponseEntity<String> forwardToUserClient(String method, Map<String, String> headers, String body, String relativeUrl) {
        switch (method) {
            case "GET":
                return userClient.forwardGetRequest(headers, relativeUrl);
            case "POST":
                return userClient.forwardPostRequest(headers, body, relativeUrl);
            case "PUT":
                return userClient.forwardPutRequest(headers, body, relativeUrl);
            case "DELETE":
                return userClient.forwardDeleteRequest(headers, body, relativeUrl);
            case "PATCH":
                return userClient.forwardPatchRequest(headers, body, relativeUrl);
            default:
                throw new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다: " + method);
        }
    }

    private ResponseEntity<String> forwardToLectureClient(String method, Map<String, String> headers, String body, String relativeUrl) {
        switch (method) {
            case "GET":
                return lectureClient.forwardGetRequest(headers, relativeUrl);
            case "POST":
                return lectureClient.forwardPostRequest(headers, body, relativeUrl);
            case "PUT":
                return lectureClient.forwardPutRequest(headers, body, relativeUrl);
            case "DELETE":
                return lectureClient.forwardDeleteRequest(headers, body, relativeUrl);
            case "PATCH":
                return lectureClient.forwardPatchRequest(headers, body, relativeUrl);
            default:
                throw new IllegalArgumentException("지원하지 않는 HTTP 메서드입니다: " + method);
        }
    }
}
