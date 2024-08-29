package com.allcleargateway.gateway;

import com.allcleargateway.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "lectureClient", url = "https://lecture.allclear-server.com:8083", configuration = FeignConfig.class)
public interface LectureClient {

    @GetMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardGetRequest(
            @RequestHeader Map<String, String> headers,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PostMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPostRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PutMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPutRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @DeleteMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardDeleteRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody(required = false) String body, // DELETE에서도 body 허용
            @PathVariable("relativeUrl") String relativeUrl
    );

    @PatchMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardPatchRequest(
            @RequestHeader Map<String, String> headers,
            @RequestBody String body,
            @PathVariable("relativeUrl") String relativeUrl
    );
}
