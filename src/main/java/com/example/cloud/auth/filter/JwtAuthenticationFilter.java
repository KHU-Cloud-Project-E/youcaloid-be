package com.example.cloud.auth.filter;

import com.example.cloud.auth.provider.JwtTokenProvider;
import com.example.cloud.config.CustomAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.cloud.config.Code.EMPTY_JWT;



@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /*
     * Header에서 JWT Token을 추출하고 토큰의 유효성을 검사한다.
     * */


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void  doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException{

        System.out.println(servletRequest.getRequestURI());
        if(servletRequest.getRequestURI().contains("/oauth2/callback") || servletRequest.getRequestURI().contains("/login")){

            filterChain.doFilter(servletRequest, servletResponse);
        }else{
            //1. Request Header 에서 JWT Token 추출
            String token = jwtTokenProvider.resolveToken(servletRequest);
            //2. validateToken 메서드로 토큰 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new CustomAuthenticationException(EMPTY_JWT.toString());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }


}
