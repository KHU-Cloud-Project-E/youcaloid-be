package com.example.cloud.auth.filter;

import com.example.cloud.config.BaseResponse;
import com.example.cloud.config.Code;
import com.example.cloud.config.CustomAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Exception Filter
 * @Description Filter 단에서 발생하는 예외를 처리하는 핸들러
 * */
@Slf4j
@Component
public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch(CustomAuthenticationException authenticationException) {
            log.error("Exception Filter (CustomAuthenticationException): " + authenticationException.getMessage());
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, authenticationException);
        } catch (Exception e) {
            log.error("Exception Filter (Exception): " + e.getMessage());
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable e) {
        response.setStatus(status.value());
        response.setContentType("application/json");
        BaseResponse<String> baseResponse = new BaseResponse<>(Code.INTERNAL_SERVER_ERROR, e.getMessage());

        try {
            PrintWriter writer = response.getWriter();
            String json = new ObjectMapper().writeValueAsString(baseResponse);
            writer.write(json);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            e.printStackTrace();
        }
    }
}
