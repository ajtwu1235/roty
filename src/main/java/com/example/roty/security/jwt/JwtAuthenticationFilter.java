//package com.example.roty.security.jwt;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//        System.out.println("JWT 테스트 시작");
//
//        ObjectMapper om = new ObjectMapper();
//        LoginRequestDto loginRequestDto = null;
//        try {
//            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("JwtAuthenticationFilter : "+loginRequestDto);
//
//        // 유저네임패스워드 토큰 생성
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(
//                        loginRequestDto.getUsername(),
//                        loginRequestDto.getPassword());
//
//        System.out.println("JwtAuthenticationFilter : 토큰생성완료");
//
//        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
//        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
//        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
//        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
//        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.
//
//        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
//        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
//        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
//        Authentication authentication =
//                authenticationManager.authenticate(authenticationToken);
//
//        PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
//        System.out.println("Authentication : "+principalDetailis.getUser().getUsername());
//        return authentication;
//    }
//}
