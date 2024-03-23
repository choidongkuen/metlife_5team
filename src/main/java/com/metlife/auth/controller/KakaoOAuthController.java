package com.metlife.auth.controller;

import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.auth.service.KakaoOAuthService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class KakaoOAuthController {

    private final KakaoOAuthService kakaoOAuthService;

    @GetMapping("/kakao/login/callback")
    public ResponseEntity<ServiceToken> kakaoOAuthLogin(
        @RequestParam String code
    ) throws IOException {
        return ResponseEntity.ok(kakaoOAuthService.kakaoOAuthLogin(code));
    }
}
