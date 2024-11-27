package com.example.gym.controllers;

import com.example.gym.dtos.LoginPasswordDto;
import com.example.gym.services.AuthenticateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticateController {
    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginPasswordDto loginPasswordDto){
         if(authenticateService.authenticate(loginPasswordDto)){
             return ResponseEntity.ok("login successfully");
         }else {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
         }

    }

}
