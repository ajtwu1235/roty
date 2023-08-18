package com.example.roty.security.oauth.provider;

import com.example.roty.security.oauth.Oauth2UserInfo;

import java.util.Map;


//카카오를 받아낼 그릇이다.
public class KakaoUserInfo implements Oauth2UserInfo {

    private Map<String,Object> attributes;
    private Map<String, Object> attributesAccount;
    private Map<String,Object> attributeProfile;

    //데이터 정제
    public  KakaoUserInfo(Map<String,Object> attributes){

        System.out.println(attributes);
        this.attributes=attributes;
        this.attributesAccount = (Map<String, Object>)  attributes.get("kakao_account");
        this.attributeProfile = (Map<String, Object>) attributesAccount.get("profile");
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
        return attributesAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return attributeProfile.get("nickname").toString();
    }
}
