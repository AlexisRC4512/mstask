package com.project.mstask.feingclient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "MS-SECURITY")
public interface UserClient {
    @PostMapping("/api/v1/autenticacion/validateToken")
    Boolean validateApiToken(@RequestParam String token);
}
