package com.example.cloud.auth.service;

import com.example.cloud.auth.info.OAuth2UserInfo;
import com.example.cloud.auth.info.OAuth2UserInfoFactory;
import com.example.cloud.auth.info.UserPrincipal;
import com.example.demo.auth.UserPrincipal;
import com.example.demo.domain.mapping.Role;
import com.example.demo.domain.mapping.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{


        private final UserRepository userRepository;
        //User을 load한다.
        @Override
        public OAuth2User loadUser (OAuth2UserRequest oAuth2UserRequest){
        System.out.println("here UserService - loadUser");
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        return processOAuth2User(oAuth2UserRequest, oAuth2User);

        }

        protected OAuth2User processOAuth2User (OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){
            try {
                String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
                String userName = oAuth2UserRequest.getClientRegistration().getProviderDetails()
                        .getUserInfoEndpoint().getUserNameAttributeName();

                System.out.println("here UserService - process");
                System.out.println("registerationId: " + registrationId + "userName" + userName);
                OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo("KAKAO", oAuth2User.getAttributes());
                User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
                //System.out.println(user.getId());
                if (user != null) {
                    //회원가입이 진행된 경우이다.
                    System.out.println("CustomService: already Join");
                } else {
                    //회원가입 진행할 것
                    System.out.println("CustomService: 회원가입 진행");
                    user = registerUser(oAuth2UserInfo);
                }
                return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
            }catch (Exception e){
                System.out.println("Custon User err"+ e);
                return null;
            }
        }

        private User registerUser (OAuth2UserInfo oAuth2UserInfo){
        System.out.println("user riegister");
        User user = User.builder()
                .email(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getName())
                .register_id(oAuth2UserInfo.getOAuth2Id())
                .status(2)
                .role(Role.ROLE_USER)
                .platform(1)
                .build();
        return userRepository.save(user);
    }

}
