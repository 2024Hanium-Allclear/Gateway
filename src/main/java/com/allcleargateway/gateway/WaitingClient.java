package com.allcleargateway.gateway;

import com.allcleargateway.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "waitingClient", url = "https://waiting.allclear-server.com:8081", configuration = FeignConfig.class)
public interface WaitingClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{relativeUrl}")
    ResponseEntity<String> forwardRequest(@RequestHeader Map<String, String> headers, @RequestBody(required = false) String body, @PathVariable("relativeUrl") String relativeUrl);
}
