package com.allcleargateway.gateway;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ApiGatewayController {

    private final RestTemplate restTemplate;

    public ApiGatewayController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/waiting/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardWaitingRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "http://waiting.allclear-server.com:8081", "/waiting");
    }

    @RequestMapping(value = "/user/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardUserRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "http://user.allclear-server.com:8082", "/user");
    }

    @RequestMapping(value = "/lecture/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public ResponseEntity<String> forwardLectureRequest(HttpServletRequest request) throws IOException {
        return forwardRequest(request, "http://lecture.allclear-server.com:8083", "/lecture");
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, String targetBaseUrl, String pathPrefix) throws IOException {
        String requestURI = request.getRequestURI();
        String targetUrl = targetBaseUrl + requestURI.replace(pathPrefix, "");

        // Create headers
        HttpHeaders headers = new HttpHeaders();

        // Set content type if available
        if (request.getContentType() != null) {
            headers.setContentType(org.springframework.http.MediaType.valueOf(request.getContentType()));
        }

        // Convert headers
        Map<String, List<String>> headerMap = Collections.list(request.getHeaderNames()).stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        headerName -> Collections.list(request.getHeaders(headerName)).stream()
                                .collect(Collectors.toList())
                ));

        headers.putAll(headerMap);

        BufferedReader reader = request.getReader();
        String body = reader.lines().collect(Collectors.joining("\n"));

        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(targetUrl, HttpMethod.valueOf(request.getMethod()), requestEntity, String.class);

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}
