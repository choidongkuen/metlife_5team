package com.metlife.user.repository;

import com.metlife.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByOauthId(String oAuthId);

    Optional<User> findByEmail(String email);
}
