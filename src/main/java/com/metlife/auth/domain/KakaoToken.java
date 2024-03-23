package com.metlife.auth.domain;


import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoToken(
    @JsonProperty("token_type")
    String tokenType,
    @JsonProperty("access_token")
    String accessToken,
    String scope
) {}
