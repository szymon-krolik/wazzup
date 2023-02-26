package com.wazzup.service;


import com.wazzup.entity.ConfirmationToken;
import com.wazzup.entity.User;
import com.wazzup.exception.TokenNotFoundException;
import com.wazzup.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Szymon Królik
 */
@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository CONFIRMATION_TOKEN_REPOSITORY;

    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        CONFIRMATION_TOKEN_REPOSITORY.save(confirmationToken);
    }

    public void deleteConfirmationToken(Long confirmationToken) {
        CONFIRMATION_TOKEN_REPOSITORY.deleteById(confirmationToken);
    }

    public void saveForgotPasswordToken(ConfirmationToken forgotPasswordToken) {
        CONFIRMATION_TOKEN_REPOSITORY.save(forgotPasswordToken);
    }

    public void deleteForgotPasswordToken(Long forgotPasswordToken) {
        CONFIRMATION_TOKEN_REPOSITORY.deleteById(forgotPasswordToken);
    }

    public ConfirmationToken getByToken(String token) {
        return CONFIRMATION_TOKEN_REPOSITORY.findConfirmationTokenByConfirmationToken(token).orElseThrow(() -> new TokenNotFoundException());
    }

    public void deleteByUser(User user) {
        CONFIRMATION_TOKEN_REPOSITORY.deleteByUserEquals(user);
    }
}