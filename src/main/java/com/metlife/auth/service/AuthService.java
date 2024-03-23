package com.metlife.auth.service;

import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.core.exception.CustomException;
import com.metlife.core.exception.ErrorCode;
import com.metlife.core.service.JwtService;
import com.metlife.core.service.RedisService;
import com.metlife.user.domain.User;
import com.metlife.user.service.UserConnector;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final JwtService jwtService;
  private final RedisService redisService;
  private final UserConnector userConnector;

  public ServiceToken reissue(HttpServletRequest request) {
    String refreshToken = jwtService.extractRefreshToken(request);
    String accessToken = jwtService.extractAccessToken(request);

    checkRefreshTokenValidation(refreshToken);
    User user = findUserByAccessToken(accessToken);
    checkRefreshTokenMatch(user, refreshToken);

    return jwtService.createServiceToken(user);
  }


  private void checkRefreshTokenValidation(String refreshToken) {
    if(!jwtService.validateToken(refreshToken)) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_IS_NOT_VALID);
    }
  }

  private void checkRefreshTokenMatch(User user, String refreshToken) {
    if(!redisService.getData(user.getId().toString()).equals(refreshToken)) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_IS_NOT_VALID);
    }
  }

  public User findUserByAccessToken(String accessToken) {
    return userConnector.findByUserId(jwtService.getUserIdFromJWT(accessToken))
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
  }

}
