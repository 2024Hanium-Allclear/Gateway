package com.allcleargateway.gateway;

import com.allcleargateway.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "waitingClient", url = "https://waiting.allclear-server.com:8081", configuration = FeignConfig.class)
public interface WaitingClient {

    @GetMapping(value = "/{relativeUrl}")
    ResponseEntity<String> forwardGetRequest(
            @RequestHeader Map<String, String> headers,
            @PathVariable("relativeUrl") String relativeUrl
    );

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH}, value = "/{relativeUrl}")
    ResponseEntity<String> forwardRequestWithBody(
            @RequestHeader Map<String, String> headers,
            @RequestBody(required = false) String body,
            @PathVariable("relativeUrl") String relativeUrl
    );
}
