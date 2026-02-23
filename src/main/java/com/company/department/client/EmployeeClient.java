package com.company.department.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.company.department.dto.EmployeeDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeClient {
	
	 private final HttpServletRequest request;

	    private final WebClient webClient =
	            WebClient.builder()
	                    .baseUrl("http://employee-service:8083")
	                    .build();

	    public List<EmployeeDto> getEmployeesByDepartment(Long deptId){

	        String authHeader = request.getHeader("Authorization");

	        return webClient.get()
	                .uri("/api/employees/department/{id}", deptId)
	                .header("Authorization", authHeader)
	                .retrieve()
	                .bodyToFlux(EmployeeDto.class)
	                .collectList()
	                .block();
	    }
	  
	    
}
