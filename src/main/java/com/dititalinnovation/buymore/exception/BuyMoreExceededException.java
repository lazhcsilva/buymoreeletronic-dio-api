package com.dititalinnovation.buymore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BuyMoreExceededException extends Exception{

    public BuyMoreExceededException(Long id, int quantityToIncrement){
        super(String.format("Electronic with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }

}
