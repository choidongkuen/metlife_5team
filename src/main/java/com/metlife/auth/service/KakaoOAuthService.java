package com.metlife.auth.service;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import com.metlife.auth.domain.KakaoOAuthUserInfo;
import com.metlife.auth.domain.KakaoToken;
import com.metlife.auth.domain.response.ServiceToken;
import com.metlife.core.service.JwtService;
import com.metlife.user.domain.User;
import com.metlife.user.service.UserConnector;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

  private static final String REGISTRATION_ID = "kakao";

  private final InMemoryClientRegistrationRepository inMemoryClientRegistrationRepository;
  private final JwtService jwtService;
  private final UserConnector userConnector;


  public ServiceToken kakaoOAuthLogin(String authorizationCode) throws IOException {
    User user = findAndSaveUser(authorizationCode);
    return jwtService.createServiceToken(user);
  }

  private User findAndSaveUser(String authorizationCode) throws IOException {
    ClientRegistration kakaoClientRegistration
        = inMemoryClientRegistrationRepository.findByRegistrationId(REGISTRATION_ID);

    KakaoToken kakaoToken = getKakaoToken(authorizationCode, kakaoClientRegistration);
    return saveUserWithKakaoUserInfo(kakaoToken, kakaoClientRegistration);
  }

  private KakaoToken getKakaoToken(String authorizationCode,
      ClientRegistration kakaoClientRegistration) {
    return WebClient.create()
        .post()
        .uri(kakaoClientRegistration.getProviderDetails().getTokenUri())
        .headers(header -> {
          header.setContentType(APPLICATION_FORM_URLENCODED);
          header.setAcceptCharset(Collections.singletonList(UTF_8));
        })
        .bodyValue(requestKakaoAuthorizationToken(authorizationCode, kakaoClientRegistration))
        .retrieve()
        .bodyToMono(KakaoToken.class)
        .block();
  }

  private MultiValueMap<String, String> requestKakaoAuthorizationToken(String code,
      ClientRegistration kakaoClientRegistration) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("code", code);
    formData.add("redirect_uri", kakaoClientRegistration.getRedirectUri());
    formData.add("grant_type", kakaoClientRegistration.getAuthorizationGrantType().getValue());
    formData.add("client_id", kakaoClientRegistration.getClientId());
    formData.add("client_secret", kakaoClientRegistration.getClientSecret());
    return formData;
  }

  private User saveUserWithKakaoUserInfo(KakaoToken kakaoToken,
      ClientRegistration kakaoClientRegistration)
      throws IOException {
    Map<String, Object> attributes = getUserAttributes(kakaoToken, kakaoClientRegistration);

    KakaoOAuthUserInfo kakaoOAuthUserInfo = new KakaoOAuthUserInfo(attributes);
    String oAuthId = kakaoOAuthUserInfo.getOAuthId();

    return userConnector.findByOAuthId(oAuthId).orElseGet(() ->
        userConnector.saveUser(User.builder()
            .oauthId(oAuthId)
            .name(kakaoOAuthUserInfo.getName())
            .build()));
  }

  private Map<String, Object> getUserAttributes(KakaoToken kakaoToken,
      ClientRegistration kakaoClientRegistration) {
    return WebClient.create()
        .get()
        .uri(kakaoClientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
        .headers(h -> h.setBearerAuth(kakaoToken.accessToken()))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
        })
        .block();
  }
}
