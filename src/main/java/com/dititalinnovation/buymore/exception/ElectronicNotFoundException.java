package com.dititalinnovation.buymore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ElectronicNotFoundException extends Exception{

    public ElectronicNotFoundException(String electronicName){
        super(String.format("Electronic with name %s not found in the system.", electronicName));
    }

    public ElectronicNotFoundException(Long id){
        super(String.format("Electronic with id %s not found in the system.", id));
    }

}
