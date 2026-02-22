package com.company.department.exception;

public class ResourceNotFoundException extends RuntimeException {
	
    public ResourceNotFoundException(String message){
        super(message);
    }


}
