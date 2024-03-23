package com.metlife.auth.domain;

import java.util.Map;

public record KakaoOAuthUserInfo(
    Map<String, Object> attributes

) {

    public String getProvider(){
        return "kakao";
    }

    public String getOAuthId() {
        return String.valueOf(attributes.get("id"));
    }

    public String getName() {
        return String.valueOf(getProfile().get("nickname"));
    }

    private Map<String,Object> getProfile() {
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }

    private Map<String,Object> getKakaoAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }
}
