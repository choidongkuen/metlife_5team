package com.metlife.core.service;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.core.property.JwtProperty;
import com.metlife.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperty jwtProperty;
  private final RedisService redisService;
  private Key key;

  @PostConstruct
  void init() {
    byte[] secretKey = Decoders.BASE64.decode(jwtProperty.getSecret());
    key = Keys.hmacShaKeyFor(secretKey);
  }

  public String extractAccessToken(HttpServletRequest request) {
    String accessToken = request.getHeader(jwtProperty.getAccess().getHeader());
    if (!ObjectUtils.isEmpty(accessToken) && accessToken.toLowerCase()
        .startsWith(jwtProperty.getBearer().toLowerCase())) {
      return accessToken.substring(jwtProperty.getBearer().length()).trim();
    }
    return null;
  }

  public String extractRefreshToken(HttpServletRequest request) {
    String refreshToken = request.getHeader(jwtProperty.getRefresh().getHeader());
    if (!ObjectUtils.isEmpty(refreshToken)) {
      return refreshToken;
    }
    return null;
  }

  public Long getUserIdFromJWT(String token) {
    try {
      Claims claims = getClaims(token);
      return Long.valueOf(claims.get("id", String.class));
    } catch (ExpiredJwtException exception) {
      return Long.valueOf(exception.getClaims().get("id").toString());
    }

  }

  public ServiceToken createServiceToken(User user) {
    String accessToken = createAccessToken(user.getId().toString());
    String refreshToken = createRefreshToken();

    redisService.setDataExpiration(user.getId().toString(), refreshToken,
        jwtProperty.getRefresh().getExpiration());

    return new ServiceToken(accessToken, refreshToken);
  }

  private String createAccessToken(String userId) {
    return createToken(userId, jwtProperty.getAccess().getExpiration());
  }

  private String createRefreshToken() {
    return createToken(UUID.randomUUID().toString(), jwtProperty.getRefresh().getExpiration());
  }

  public boolean validateToken(String token) {
    try {
      Claims claims = getClaims(token);
      return !claims.getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      log.error("JWT 가 만료되었습니다.");
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 JWT 입니다.");
    } catch (IllegalArgumentException e) {
      log.error("JWT 가 잘못되었습니다.");
    }
    return false;
  }


  private String createToken(String userId, Long tokenExpiration) {

    Date tokenExpiresIn = new Date(new Date().getTime() + tokenExpiration);

    return Jwts.builder()
        .setSubject("ACCESSTOKEN")
        .claim("id", userId)
        .signWith(key, HS512)
        .setExpiration(tokenExpiresIn)
        .compact();
  }

  private Claims getClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

}
