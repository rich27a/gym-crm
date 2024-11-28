package com.example.gym.services;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.repositories.UserRepository;
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

    public boolean changePassword(PasswordChangeDto passwordChangeDto){
        LoginPasswordDto loginPasswordDto = new LoginPasswordDto(passwordChangeDto.getUsername(), passwordChangeDto.getOldPassword());
        if(authenticate(loginPasswordDto)){

        }
        return false;
    }
}
