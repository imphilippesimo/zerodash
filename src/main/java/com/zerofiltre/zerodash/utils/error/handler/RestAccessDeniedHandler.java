package com.zerofiltre.zerodash.utils.error.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerofiltre.zerodash.utils.Constants;
import com.zerofiltre.zerodash.utils.error.model.RestAPIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Exception handler intercepting unauthorized access request at the filter level,
 * before the rest/graphql resources.
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        RestAPIResponse response = new RestAPIResponse(HttpStatus.FORBIDDEN, Constants.UNAUTHORIZED_ACCESS_DENIED);
        OutputStream out = httpServletResponse.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, response);
        out.flush();
    }
}
