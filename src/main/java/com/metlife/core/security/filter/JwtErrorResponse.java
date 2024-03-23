package com.metlife.core.security.filter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtErrorResponse {

    private String message;

    public static JwtErrorResponse of(Throwable exception) {
        return new JwtErrorResponse(exception.getMessage());
    }

    public static JwtErrorResponse of(String message) {
        return new JwtErrorResponse(message);
    }
}
