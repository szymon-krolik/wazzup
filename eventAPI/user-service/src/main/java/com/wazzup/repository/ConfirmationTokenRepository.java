package com.wazzup.repository;

import com.wazzup.entity.ConfirmationToken;
import com.wazzup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Szymon Królik
 */
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
    void deleteByUserEquals(User user);
}
