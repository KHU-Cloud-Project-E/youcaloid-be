package com.example.cloud.auth.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        System.out.println("here is Entry Point");
        PrintWriter printWriter = response.getWriter();
        response.setContentType("application/json;charset=utf-8");
        printWriter.write("{false \n "+HttpServletResponse.SC_UNAUTHORIZED+"\n"+ authException.getMessage()+"}");

    }
}
