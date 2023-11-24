package com.example.cloud.auth.handler;

import com.example.cloud.auth.provider.JwtTokenProvider;
import com.example.demo.auth.provider.JwtTokenProvider;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    /*
    * 인증 성공시 JWT토큰 발급
    * */

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        try {

            String email = (String) authentication.getName();
            Integer status = userRepository.findStatusByEmail(email).get().getStatus();
            //Optional 객체는 이와 같이 .get()하고 .메소드를 사용해 값을 떼오면 된다.


            System.out.println("status:" + status);
            if (response.isCommitted()) {
                logger.debug("response is commited() ");
                return;
            }

            String access = jwtTokenProvider.generateToken(authentication).getAccessToken();
            String refresh = jwtTokenProvider.generateToken(authentication).getRefreshToken();
            userRepository.updateUserRefreshToken(refresh,userRepository.findByEmail(email).get().getId());
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            queryParams.add("access_token", access);
            queryParams.add("refresh_token",refresh);
            String URL = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("localhost")
                    .port("8080")
                    .path("/users/callback")
                    .queryParams(queryParams)
                    .toUriString();
           System.out.println(URL);
            getRedirectStrategy().sendRedirect(request, response, URL); //성공 시점에 redirect

        }catch (Exception err){
            System.out.println(err);
        }

    }

    private String makeRedirectUrl(String token) {
        try {
            return UriComponentsBuilder.fromUriString("http://localhost:8080/oauth2/callback")
                    .queryParam("token", token)
                    .build().toUriString();
        }catch(Exception err){
            System.out.println(err);
            return null;
        }
    }




}
