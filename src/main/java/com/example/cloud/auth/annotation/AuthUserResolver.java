package com.example.cloud.auth.annotation;


import com.example.cloud.domain.mapping.User;
import com.example.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
@RequiredArgsConstructor

public class AuthUserResolver implements HandlerMethodArgumentResolver {


    private final UserRepository userRepository;


    @Override
    public boolean supportsParameter(MethodParameter methodParameter){

        //AuthUser 사용중인지 확인한다.
        AuthUser authUser = methodParameter.getParameterAnnotation(AuthUser.class);
        if(authUser==null){

            return false;
        }

        //User type인지 확인한다.
        if(methodParameter.getParameterType().equals(User.class)==false){

            return false;
        }

        return true;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception{

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = null;
        if(authentication != null){

            principal = authentication.getPrincipal();

        }
        if(principal==null){

            return null;
        }

        Optional<User> userObject = userRepository.findByEmail(authentication.getName());
        if(userObject.isPresent()) {
            User user = User.builder()
                    .id(userObject.get().getId())
                    .email(principal.getClass().getName())
                    .role(userObject.get().getRole())
                    .status(userObject.get().getStatus())
                    .nickname(userObject.get().getNickname())
                    .register_id(userObject.get().getRegister_id())
                    .refresh_token(userObject.get().getRefresh_token())
                    .build();
            return user;
        }
        return null;

    }


}
