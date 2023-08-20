package com.example.roty.security.oauth;

import com.example.roty.User.domain.User;
import com.example.roty.User.repository.UserRepository;
import com.example.roty.security.oauth.provider.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;



// 여기가 Oauth로그인 후처리 서비스다.
@Service
@RequiredArgsConstructor
public class Oauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    //여기가 후처리 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);


        return processOAuth2User(userRequest,oAuth2User);
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

            //토큰에 담을 것user
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

//        PrincipalDetails principalDetails = new PrincipalDetails(user);
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(
//                        principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
//                        null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
//                        principalDetails.getAuthorities());
//
//        // 강제로 시큐리티의 세션에 접근하여 값 저장
//        SecurityContextHolder.getContext().setAuthentication(authentication);

//
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            Authentication authentication2 = new UsernamePasswordAuthenticationToken(
//                    principalDetails,
//                    null,
//                    principalDetails.getAuthorities()
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication2);
//        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
