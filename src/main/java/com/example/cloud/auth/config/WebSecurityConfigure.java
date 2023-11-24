package com.example.cloud.auth.config;


import com.example.cloud.auth.filter.ExceptionFilter;
import com.example.cloud.auth.filter.JwtAuthenticationFilter;
import com.example.cloud.auth.handler.FailureHandler;
import com.example.cloud.auth.handler.JwtAccessDeniedHandler;
import com.example.cloud.auth.handler.SuccessHandler;
import com.example.cloud.auth.jwt.JwtAuthenticationEntryPoint;
import com.example.cloud.auth.provider.JwtTokenProvider;
import com.example.cloud.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigure {


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) ->web.ignoring()
                .antMatchers("/users/autologin","/test/**","/terms/**","/health/**","/images/**","/post","/users/autologin/**","/users/signup/**","/v3/**","/swagger-ui/**")
                .antMatchers(HttpMethod.GET, "/users/callback/**","/test/**","/posts/**")
                .antMatchers(HttpMethod.POST, "/posts");
    }

    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtTokenProvider jwtTokenProvider;

    private final FailureHandler failureHandler;

    private final SuccessHandler successHandler;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //disable 설정
        http
                .httpBasic().disable() //HTTP Basic Auth 기반 로그인 창이 뜨지 않는다.
                .csrf().disable() // REST API의 경우 csrf 보안이 필요하지 않는다.
                .formLogin().disable() //formLogin disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //세션 사용하지 않음 >> JWT 등을 사용할 때 사용하는 것

        //리소스 인증 및 권한 설정

        http.authorizeRequests().antMatchers("/").permitAll();

        http.authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/users/oauth2/").permitAll()
                .antMatchers("/posts/").permitAll()
                .antMatchers("/images/").permitAll()
                .antMatchers("/terms/").permitAll()
                .antMatchers("/postImages/**").authenticated();


        http.exceptionHandling()
                //.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //.accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .oauth2Login()
                .authorizationEndpoint().baseUri("/oauth2/authorize/**")
                .and()
                .redirectionEndpoint().baseUri("/oauth2/callback/**")
                .and()
                .userInfoEndpoint().userService(customOAuth2UserService)
                .and()
                .successHandler(successHandler);

        //.failureHandler(failureHandler);


        // Custom filter 적용
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new ExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();

    }


}
