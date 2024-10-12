package com.koonetto.koonetto_resource_server.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koonetto.koonetto_resource_server.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.Map;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse<Map<String, ?>> errorResponse = new ErrorResponse<>();
        errorResponse.setError(Map.of(
                "errorCode", 403,
                "message", accessDeniedException.getMessage(),
                "path", request.getServletPath()
        ));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
