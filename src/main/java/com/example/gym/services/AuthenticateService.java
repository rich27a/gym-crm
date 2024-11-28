package com.example.gym.services;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(LoginPasswordDto loginPasswordDto){
        return userRepository.findByUsername(loginPasswordDto.getUsername())
                .map(user -> passwordEncoder.matches(loginPasswordDto.getPassword(), user.getPassword()))
                .orElse(false);
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
