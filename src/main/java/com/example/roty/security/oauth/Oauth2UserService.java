package com.example.roty.security.oauth;

import com.example.roty.User.domain.User;
import com.example.roty.User.repository.UserRepository;
import com.example.roty.security.oauth.provider.KakaoUserInfo;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;
import java.util.Optional;



// 여기가 Oauth로그인 후처리 서비스다.
@Service
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final Key secretKey;

    public Oauth2UserService(UserRepository userRepository,@Value("${jwt.secret}") String secretKey) {

        this.userRepository=userRepository;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }


    //여기가 후처리 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return  processOAuth2User(userRequest,oAuth2User);
    }


    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        Oauth2UserInfo oAuth2UserInfo = null;

//        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
//            System.out.println("구글 로그인 요청~~");
//            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
//        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
//            System.out.println("페이스북 로그인 요청~~");
//            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
//        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
//            System.out.println("네이버 로그인 요청~~");
//            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
//        } else {
//            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎ");
//        }


        OAuth2User oauth2User =super.loadUser(userRequest);

        oAuth2UserInfo= new KakaoUserInfo(oauth2User.getAttributes());

        //System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
        //System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());

        //중복 체크
        Optional<User> userOptional =
                userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = User.builder()
                    .username(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())

                    .build();
            userRepository.save(user);
        }

        //JWT 토큰 생성
        String compact = Jwts.builder()
                .claim("userId",user.getUserId() )
                .claim("email",oAuth2UserInfo.getEmail())
                .claim("name", user.getUsername())
//                .claim("age", principal.getAge())
                .setExpiration(new Date(System.currentTimeMillis() + 120_000))
                .signWith(secretKey)
                .compact();

        System.out.println(compact);

        System.out.println("실행완료");

        return new PrincipalDetails(user, oAuth2User.getAttributes(),compact);
    }

}
