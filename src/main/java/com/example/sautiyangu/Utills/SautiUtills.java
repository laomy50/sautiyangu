package com.example.sautiyangu.Utills;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SautiUtills {

    private SautiUtills(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);
    }
}
