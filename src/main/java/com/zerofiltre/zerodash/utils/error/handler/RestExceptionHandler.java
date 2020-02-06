package com.zerofiltre.zerodash.utils.error.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

     //Every rest api related error should be returned to the client as a zalando Problem

}
