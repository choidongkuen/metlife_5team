package com.metlife.core.security;

import com.metlife.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class UserAuthentication extends AbstractAuthenticationToken {

  private final String oAuthId;
  private final String email;
  private Long id;

  public UserAuthentication(User user) {
    super(authorities(user));
    this.id = user.getId();
    this.email = user.getEmail();
    this.oAuthId = user.getOauthId();
  }

  private static List<GrantedAuthority> authorities(User User) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    return authorities;
  }


  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return id;
  }
}
