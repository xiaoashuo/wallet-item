package com.lovecyy.wallet.item.common;

import com.lovecyy.wallet.item.model.dto.R;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandleResolver {

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public R handle(ConstraintViolationException ex){
        return R.fail(400,ex.getLocalizedMessage());
    }
}
