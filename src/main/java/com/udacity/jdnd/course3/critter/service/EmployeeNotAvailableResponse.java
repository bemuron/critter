package com.udacity.jdnd.course3.critter.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No employee available for the specified date")
public class EmployeeNotAvailableResponse extends RuntimeException {
    public EmployeeNotAvailableResponse(){}
    public EmployeeNotAvailableResponse(String message){
        super(message);
    }
}
