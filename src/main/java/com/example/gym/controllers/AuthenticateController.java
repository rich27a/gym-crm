package com.example.gym.controllers;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.dtos.PasswordChangeDto;
import com.example.gym.services.AuthenticateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {
    private final AuthenticateService authenticateService;
    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }
    @GetMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginPasswordDto loginPasswordDto){
        return Optional.of(authenticateService.authenticate(loginPasswordDto))
                .filter(auth -> auth)
                .map(auth -> ResponseEntity.ok("Login successfully"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password or username"));
    }
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto){
        return ResponseEntity.ok(null);
    }
}
