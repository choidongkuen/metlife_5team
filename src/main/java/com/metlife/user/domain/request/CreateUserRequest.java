package com.metlife.user.domain.request;

import com.metlife.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @Email
    @NotBlank
    String email,
    @NotBlank
    String name,
    @NotBlank
    String password
) {
  public User toEntity(String bcryptPassword) {
    return User.builder()
        .email(email)
        .name(name)
        .password(bcryptPassword)
        .build();
  }
}
