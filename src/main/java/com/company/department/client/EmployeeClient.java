package com.company.department.client;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.company.department.dto.EmployeeDto;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmployeeClient {
	
	 private final HttpServletRequest httpRequest;

	    private final WebClient employeeWebClient =
	            WebClient.builder()
	                    .baseUrl("http://employee-service:8083")
	                    .build();

	    @CircuitBreaker(name = "employeeService", fallbackMethod = "getEmployeesByDepartmentFallback")
	    @Retry(name = "employeeService")
	    public List<EmployeeDto> getEmployeesByDepartment(Long departmentId){
	        log.info("Fetching employees for departmentId={}", departmentId);
	        String authorizationHeader = httpRequest.getHeader("Authorization");

	        try {
	            List<EmployeeDto> departmentEmployees = employeeWebClient.get()
	                    .uri("/api/employees/department/{id}", departmentId)
	                    .header("Authorization", authorizationHeader)
	                    .retrieve()
	                    .bodyToFlux(EmployeeDto.class)
	                    .collectList()
	                    .block();
	            log.info("Successfully fetched {} employees for departmentId={}", departmentEmployees.size(), departmentId);
	            return departmentEmployees;
	        } catch (Exception serviceException) {
	        	log.warn("Employee service unavailable deptId={} reason={}", departmentId, serviceException.getMessage());
	            throw serviceException;
	        }
	    }
	    
	    private List<EmployeeDto> getEmployeesByDepartmentFallback(Long departmentId, Exception fallbackException) {
	    	log.warn("Fallback triggered for departmentId={} reason={}", departmentId, fallbackException.getMessage());
	        return List.of();
	    }
}
