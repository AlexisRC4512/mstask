package com.project.mstask;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@EnableFeignClients("com.project.mstask")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@OpenAPIDefinition
@EnableDiscoveryClient
public class MstaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(MstaskApplication.class, args);
	}

}
