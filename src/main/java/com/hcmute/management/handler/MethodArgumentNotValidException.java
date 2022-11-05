package com.hcmute.management.handler;

import org.springframework.http.HttpStatus;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
@ResponseBody
public class MethodArgumentNotValidException extends BindException {
    public MethodArgumentNotValidException(BindingResult exception) {
        super(exception);
    }
}
