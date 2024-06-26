package com.metlife.core.security.filter;


import com.metlife.auth.service.AuthService;
import com.metlife.core.property.JwtProperty;
import com.metlife.core.security.UserAuthentication;
import com.metlife.core.service.JwtService;
import com.metlife.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String ATTRIBUTE_NAME = "exception";
  private final JwtService jwtService;
  private final AuthService authService;
  private final JwtProperty jwtProperty;

  @Override
  public void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (request.getMethod().equals("OPTIONS")) {
      return;
    }

    try {
      String token = this.resolveTokenFromRequest(request);
      if (checkTokenExistenceAndValidation(request, token)) {
        User user = getUser(token);
        saveUserAuthentication(user);
      }
    } catch (InsufficientAuthenticationException e) {
      log.error("JwtAuthentication UnauthorizedUserException!");
    }
    filterChain.doFilter(request, response);
  }

  private User getUser(String token) {
    return this.authService.findUserByAccessToken(token);
  }

  private boolean checkTokenExistenceAndValidation(HttpServletRequest request, String token) {
    if (!StringUtils.hasText(token)) {
      setRequestAttribute(request, "요청에 대한 JWT 정보가 존재하지 않습니다.");
      return false;
    }
    if (!jwtService.validateToken(token)) {
      setRequestAttribute(request, "요청에 대한 JWT 가 유효하지 않습니다.");
      return false;
    }
    return true;
  }

  private void saveUserAuthentication(User user) {
    UserAuthentication authentication = new UserAuthentication(user);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private String resolveTokenFromRequest(HttpServletRequest request) {
    String token = request.getHeader(jwtProperty.getAccess().getHeader());
    if (StringUtils.hasText(token) && token.toLowerCase().startsWith(jwtProperty.getBearer().toLowerCase())) {
      return token.substring(7);
    }
    return null;
  }

  private void setRequestAttribute(HttpServletRequest request, String name) {
    request.setAttribute(ATTRIBUTE_NAME, name);
  }
}
