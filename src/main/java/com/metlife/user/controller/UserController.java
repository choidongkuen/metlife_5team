package com.metlife.user.controller;

import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.user.domain.request.CreateUserRequest;
import com.metlife.user.domain.request.LoginUserRequest;
import com.metlife.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  /* 1. 유저 일반 회원가입 */
  @PostMapping("/signup")
  public Long signUp(@Valid @RequestBody CreateUserRequest request) {
    return userService.createUser(request);
  }

  /* 2. 유저 일반 로그인 */
  @PostMapping("/login")
  public ResponseEntity<ServiceToken> login(@Valid @RequestBody LoginUserRequest request) {
    return ResponseEntity.ok(userService.loginUser(request));

  }

}
