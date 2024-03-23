package com.metlife.auth.domain.response;

public record ServiceToken(
    String accessToken,
    String refreshToken
) {
}
