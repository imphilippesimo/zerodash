package com.zerofiltre.zerodash.utils.error.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class RestAPIResponse implements Serializable {

    private HttpStatus status;
    private String message;
    private Object result;

    public RestAPIResponse() {
    }

    public RestAPIResponse(HttpStatus status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public RestAPIResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


}
