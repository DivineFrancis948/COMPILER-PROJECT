package com.compiler.question.util;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value="auth-service",url="http://localhost:8085/register")
public interface FeignServiceUtil 
{
	@GetMapping("/demo")
	String demofeignservice();
}
	