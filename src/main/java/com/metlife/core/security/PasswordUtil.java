package com.metlife.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class PasswordUtil {
  private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /* 암호화 */
  public static String encode(String password) {
    return passwordEncoder.encode(password);
  }

  /* 비밀번호 동일 체크 */
  public static boolean isEqual(String inputPassword, String encodedPassword) {
    return passwordEncoder.matches(inputPassword, encodedPassword);
  }
}
