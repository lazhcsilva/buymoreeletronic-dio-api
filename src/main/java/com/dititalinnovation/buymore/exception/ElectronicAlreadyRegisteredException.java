package com.dititalinnovation.buymore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ElectronicAlreadyRegisteredException extends Exception{

    public ElectronicAlreadyRegisteredException(String electronicName){
        super(String.format("Electronic with name %s already registered in the system.", electronicName));
    }

}
