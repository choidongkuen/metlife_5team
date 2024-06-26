package com.metlife.core.security.filter;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private static final String ATTRIBUTE_NAME = "exception";

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authenticationException) throws IOException {
    this.sendUnAuthenticatedError(request, response, authenticationException);
  }

  private void sendUnAuthenticatedError(HttpServletRequest request, HttpServletResponse response, Exception exception)
      throws IOException {
    response.setStatus(SC_UNAUTHORIZED);
    OutputStream os = response.getOutputStream();
    log.error("Responding with unauthenticated error. Message - {}", exception.getMessage());
    objectMapper.writeValue(os, JwtErrorResponse.of((String) request.getAttribute(ATTRIBUTE_NAME)));
    os.flush();
  }
}
