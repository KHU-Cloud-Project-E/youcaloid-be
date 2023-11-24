package com.example.cloud.auth.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        /*String targetUrl = "http://localhost:8080/oauth2/callback";

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", authenticationException.getLocalizedMessage())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        */

        try{
            System.out.println("err");
        }catch (Exception ex){
            System.out.println(ex);
        }

    }


}
