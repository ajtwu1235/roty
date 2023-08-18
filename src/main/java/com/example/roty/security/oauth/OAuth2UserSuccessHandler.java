package com.example.roty.security.oauth;

import com.example.roty.User.domain.User;
import com.example.roty.User.service.UserService;
import com.example.roty.security.jwt.AuthService;
import com.example.roty.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.*;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class OAuth2UserSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.secret}")
    private String redirectUri;

    private ObjectMapper objectMapper;

    private AuthService authService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }
        PrincipalDetails o = (PrincipalDetails) authentication.getPrincipal();

        String token = authService.makeToken(o.getUser());

        writeTokenResponse(response,token);
    }

    private void writeTokenResponse(HttpServletResponse response, String token)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Authorization", token);
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();
        writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }

//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//
//        String targetUrl = "http://localhost:8080";
//
//        //JWT 생성
//        UserResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//
//        return UriComponentsBuilder.fromUriString(targetUrl)
//                .queryParam("token", tokenInfo.getAccessToken())
//                .build().toUriString();
//    }

//    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
//        super.clearAuthenticationAttributes(request);
//        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//    }

//    private boolean isAuthorizedRedirectUri(String uri) {
//        URI clientRedirectUri = URI.create(uri);
//        URI authorizedUri = URI.create(redirectUri);
//
//        if (authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
//                && authorizedUri.getPort() == clientRedirectUri.getPort()) {
//            return true;
//        }
//        return false;
//    }
}


    // Access Token 생성
//    private String delegateAccessToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", username);
//        claims.put("roles", authorities);
//
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
//
//        return accessToken;
//    }
//
//    // Refresh Token 생성
//    private String delegateRefreshToken(String username) {
//        String subject = username;
//        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
//        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
//
//        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
//
//        return refreshToken;
//    }

