package com.example.gym.services;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;

    public AuthenticateService(UserRepository userRepository, PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
    }

    public boolean authenticate(LoginPasswordDto loginPasswordDto){
        if (loginAttemptService.isBlocked(loginPasswordDto.getUsername())) {
            throw new BadCredentialsException("User is temporarily blocked due to multiple failed login attempts\"");
        }
        boolean isAuthenticated = userRepository.findByUsername(loginPasswordDto.getUsername())
                .map(user -> passwordEncoder.matches(loginPasswordDto.getPassword(), user.getPassword()))
                .orElse(false);

        if (!isAuthenticated) {
            loginAttemptService.loginFailed(loginPasswordDto.getUsername());
        } else {
            loginAttemptService.loginSucceeded(loginPasswordDto.getUsername());
        }

        return isAuthenticated;
    }

    @Transactional
    public boolean changePassword(PasswordChangeDto passwordChangeDto){
        return userRepository.findByUsername(passwordChangeDto.getUsername())
                .map(user -> {
                    if (passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())){
                        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
                        userRepository.save(user);
                        return true;
                    }
                    return false;
                }
                )
                .orElse(false);
    }
}
