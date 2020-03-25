package com.zerofiltre.zerodash.presentation.security;


import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;


@Component
public class JwtConfig {


    @Value("${zerodash.security.jwt.header}")
    private String header;

    @Value("${zerodash.security.jwt.prefix}")
    private String prefix;

    @Value("${zerodash.security.jwt.token-validity-in-seconds}")
    private int validityInSeconds;

    @Value("${zerodash.security.jwt.secret}")
    private String secret;


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getValidityInSeconds() {
        return validityInSeconds;
    }

    public void setValidityInSeconds(int validityInSeconds) {
        this.validityInSeconds = validityInSeconds;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
