package com.allcleargateway.gateway;

import com.allcleargateway.gateway.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "gatewayClient", configuration = FeignConfig.class)
public interface GatewayClient {

    @RequestMapping(method = RequestMethod.GET, value = "/{relativeUrl}")
    ResponseEntity<String> forwardRequest(@RequestHeader Map<String, String> headers, @RequestBody(required = false) String body, @RequestParam("relativeUrl") String relativeUrl);
}
