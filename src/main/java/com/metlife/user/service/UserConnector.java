package com.metlife.user.service;

import com.metlife.user.domain.User;
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
public class UserConnector {

    private final UserRepository userRepository;

    public Optional<User> findByOAuthId(String oAuthId) {
        return userRepository.findByOauthId(oAuthId);
    }

    public Optional<User> findByUserId(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
