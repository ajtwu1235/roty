package com.example.roty.security.oauth;

import com.example.roty.User.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.Map;

@Service
public class AuthService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String makeToken(User user){
        SecretKeySpec key = getKey();
        String compact = Jwts.builder()
                .claim("memberId", user.getUserId())
                .claim("username", user.getUsername())
                .claim("email",user.getEmail())

                .setExpiration(new Date(System.currentTimeMillis() + 120_000))
                .signWith(key)
                .compact();
        return compact;
    }
    public Map<String, Object> getClaims(String token){
//        SecretKeySpec key = getKey();

        return (Claims) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parse(token)
                .getBody();
    }
//    public MemberDto tokenToMemberDto(String token){
//        Map<String, Object> tokenToData = getClaims(token);
//        MemberDto memberDto = new MemberDto((Long) tokenToData.get("memberId"),  null, (String) tokenToData.get("name"), (Integer) tokenToData.get("age"));
//        return memberDto;
//    }

    private SecretKeySpec getKey() {
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
        SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), hs256.getJcaName());
        return key;
    }
}