package com.zerofiltre.zerodash.utils.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerofiltre.zerodash.utils.Constants;
import com.zerofiltre.zerodash.utils.error.model.RestAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Exception handler intercepting unauthenticated access exceptions happening at the filter level,
 * before the rest/graphql resources.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        RestAPIResponse response = new RestAPIResponse(HttpStatus.UNAUTHORIZED, Constants.ZDUSER_NOT_FOUND);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }

}