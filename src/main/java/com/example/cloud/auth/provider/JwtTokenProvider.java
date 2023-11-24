package com.example.cloud.auth.provider;


import com.example.cloud.config.CustomAuthenticationException;
import com.example.cloud.web.dto.UserResponseDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.example.cloud.config.Code.*;

@Slf4j
@Component
public class JwtTokenProvider {

    /*
    *
    * */
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";

    @Value("${jwt.access-token-validity-in-seconds}") Long access_token_validity_in_seconds;
    @Value("${jwt.refresh-token-validity-in-seconds}") Long refresh_token_validity_in_seconds;
    private final Key key;

    //토큰의 암호화 및 복호화
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //Authentication을 가지고 AccessToken, RefreshToken을 생성한다.
    public UserResponseDto.TokenInfo generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), authentication.getAuthorities());
    }

    public UserResponseDto.TokenInfo generateToken(String name, Collection<? extends GrantedAuthority> inputAuthorities)
    {
        String authorities = inputAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date now = new Date();

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + access_token_validity_in_seconds))  //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("access Token: " + accessToken);
        //Generate RefreshToken
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() +refresh_token_validity_in_seconds)) //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("refresh Token: " + refreshToken);
        return UserResponseDto.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(access_token_validity_in_seconds)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refresh_token_validity_in_seconds)
                .build();

    }

    //회원가입
    public UserResponseDto.TokenInfo generateToken(String name)
    {

        System.out.println(name);
        String authorities = "ROLE_USER";
        Date now = new Date();

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + access_token_validity_in_seconds))  //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("access Token: " + accessToken);
        //Generate RefreshToken
        String refreshToken = Jwts.builder()
                .claim("type", TYPE_REFRESH)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() +refresh_token_validity_in_seconds)) //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("refresh Token: " + accessToken);
        return UserResponseDto.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(access_token_validity_in_seconds)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(refresh_token_validity_in_seconds)
                .build();

    }

    public UserResponseDto.TokenInfo generateAccessToken(String name)
    {

        System.out.println(name);
        String authorities = "ROLE_USER";
        Date now = new Date();

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setSubject(name)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("type", TYPE_ACCESS)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + access_token_validity_in_seconds))  //토큰 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        System.out.println("access Token: " + accessToken);

        return UserResponseDto.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpirationTime(access_token_validity_in_seconds)
                .refreshToken("not new generate")
                .refreshTokenExpirationTime(refresh_token_validity_in_seconds)
                .build();

    }


    //JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            //TODO:: Change Custom Exception
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        //클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //토큰 정보를 검증하는 메서드
    public Boolean validateToken(String token) throws CustomAuthenticationException {

        try {
            System.out.println("here is token provider");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token));
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new CustomAuthenticationException((INVALID_JWT.toString()));


        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);

            throw new CustomAuthenticationException((EXPIRED_JWT.toString()));

        } catch (UnsupportedJwtException e) {
            //jwt가 예상하는 형식과 다른 형식이다.
            log.info("Unsupported JWT Token", e);

            throw new CustomAuthenticationException(UNSUPPORTED_JWT.toString());

        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);

            throw new CustomAuthenticationException(ILLEGAL_JWT.toString());

        } catch (Exception e) {
            //잘못된 jwt 구조
            log.info("JWT  ERR", e);
            throw new CustomAuthenticationException(WRONG_JWT.toString());

        }


    }


    public Boolean validateAccessToken(String token) throws CustomAuthenticationException {

        try {
            System.out.println("here is token provider");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token));
            return true;
        }   catch (UnsupportedJwtException e) {
            //jwt가 예상하는 형식과 다른 형식이다.
            log.info("Unsupported JWT Token", e);

            throw new CustomAuthenticationException(UNSUPPORTED_JWT.toString());

        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);

            throw new CustomAuthenticationException(ILLEGAL_JWT.toString());

        } catch (Exception e) {
            //잘못된 jwt 구조
            log.info("JWT  ERR", e);

        }

    return null;
    }


    public Boolean RefreshValidateToken(String token) throws CustomAuthenticationException {

        try {
            System.out.println("here is token provider");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token));
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid Refresh Token", e);
            throw new CustomAuthenticationException("Invalid Refresh Token");


        } catch (ExpiredJwtException e) {
            log.info("Expired Refresh Token GO TO LOGIN again", e);

            throw new CustomAuthenticationException("Expired Refresh Token GO TO LOGIN Again");

        } catch (UnsupportedJwtException e) {
            //jwt가 예상하는 형식과 다른 형식이다.
            log.info("Unsupported Refresh Token", e);

            throw new CustomAuthenticationException("Unsupported Refresh Token");

        } catch (IllegalArgumentException e) {
            log.info("refresh claims string is empty.", e);

            throw new CustomAuthenticationException("refresh claims string is empty.");

        } catch (Exception e) {
            //잘못된 jwt 구조
            log.info("Wrong  Refresh ERR", e);
            throw new CustomAuthenticationException("Wrong  Refresh ERR");

        }


    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            // ???
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
