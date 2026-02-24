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
	
	 private final HttpServletRequest request;

	    private final WebClient webClient =
	            WebClient.builder()
	                    .baseUrl("http://employee-service:8083")
	                    .build();

	    @CircuitBreaker(name = "employeeService", fallbackMethod = "getEmployeesByDepartmentFallback")
	    @Retry(name = "employeeService")
	    public List<EmployeeDto> getEmployeesByDepartment(Long deptId){
	        log.info("Fetching employees for departmentId={}", deptId);
	        String authHeader = request.getHeader("Authorization");

	        try {
	            List<EmployeeDto> employees = webClient.get()
	                    .uri("/api/employees/department/{id}", deptId)
	                    .header("Authorization", authHeader)
	                    .retrieve()
	                    .bodyToFlux(EmployeeDto.class)
	                    .collectList()
	                    .block();
	            log.info("Successfully fetched {} employees for departmentId={}", employees.size(), deptId);
	            return employees;
	        } catch (Exception e) {
	        	log.warn("Employee service unavailable deptId={} reason={}", deptId, e.getMessage());
	            throw e;
	        }
	    }
	    
	    private List<EmployeeDto> getEmployeesByDepartmentFallback(Long deptId, Exception e) {
	    	log.warn("Fallback triggered for departmentId={} reason={}", deptId, e.getMessage());
	        return List.of();
	    }
}
