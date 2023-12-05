package com.example.cloud.auth.config;


import com.example.cloud.auth.filter.ExceptionFilter;
import com.example.cloud.auth.filter.JwtAuthenticationFilter;
import com.example.cloud.auth.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter <DefaultSecurityFilterChain, HttpSecurity>{

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void configure(HttpSecurity http){

        // jwt filter 설정
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new ExceptionFilter(), JwtAuthenticationFilter.class);
    }
}
