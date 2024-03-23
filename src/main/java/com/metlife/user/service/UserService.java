package com.metlife.user.service;

import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.core.exception.CustomException;
import com.metlife.core.exception.ErrorCode;
import com.metlife.core.security.PasswordUtil;
import com.metlife.core.service.JwtService;
import com.metlife.user.domain.User;
import com.metlife.user.domain.request.CreateUserRequest;
import com.metlife.user.domain.request.LoginUserRequest;
import com.metlife.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final JwtService jwtService;
  @Transactional
  public Long createUser(CreateUserRequest request) {
    if(checkEmailExistence(request.email()).isPresent()) {
      throw new CustomException(ErrorCode.EMAIL_IS_DUPLICATED);
    }
    return userRepository.save(request.toEntity(PasswordUtil.encode(request.password()))).getId();
  }


  public ServiceToken loginUser(LoginUserRequest request) {
    return checkEmailExistence(request.email())
        .map(user -> {
          checkPassword(user, request.password());
          return jwtService.createServiceToken(user);
        })
        .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_IS_NOT_EXIST));
  }


  private Optional<User> checkEmailExistence(String email) {
    return userRepository.findByEmail(email);
  }

  private void checkPassword(User user, String password) {
    if(!PasswordUtil.isEqual(password,user.getPassword())) {
      throw new CustomException(ErrorCode.PASSWORD_IS_NOT_MATCH);
    }
  }
}
