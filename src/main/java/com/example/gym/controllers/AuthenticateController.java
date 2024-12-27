package com.example.gym.controllers;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.dtos.LoginResponseDTO;
import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.services.AuthenticateService;
import com.example.gym.services.TokenBlackListService;
import com.example.gym.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Authentication", description = "Controller for authentication")
@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    private final JwtUtil jwtUtil;


    public AuthenticateController(AuthenticateService authenticateService, JwtUtil jwtUtil) {
        this.authenticateService = authenticateService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginPasswordDto loginPasswordDto){
        Boolean isAuthenticated = authenticateService.authenticate(loginPasswordDto);
        if (isAuthenticated) {
            String token = jwtUtil.generateToken(loginPasswordDto.getUsername());
            return ResponseEntity.ok(
                    new LoginResponseDTO("Login successfully", token)
            );
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponseDTO("Invalid credentials", null));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return authenticateService.logout(request) ? ResponseEntity.ok("Logout successfully") : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid or empty token");
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto){
        return Optional.of(authenticateService.changePassword(passwordChangeDto))
                .filter(auth -> auth)
                .map(auth -> ResponseEntity.ok("Password changed"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password or username"));
    }
}
