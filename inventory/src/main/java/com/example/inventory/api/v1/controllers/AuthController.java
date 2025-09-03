package com.example.inventory.api.v1.controllers;

import com.example.inventory.api.v1.dto.AuthData;
import com.example.inventory.api.v1.dto.AuthResponse;
import com.example.inventory.api.v1.dto.CredentialsRequest;
import com.example.inventory.api.v1.dto.ErrorResponse;
import com.example.inventory.api.v1.dto.UserLoginResponse;
import com.example.inventory.auth.application.ports.AuthCommand;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    
    private final AuthCommand authCommand;

    public AuthController(AuthCommand authCommand) {
        this.authCommand = authCommand;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialsRequest credentials) {
        Optional<UserLoginResponse> authResponse = authCommand
            .login(credentials.nickname(), credentials.passwd());
        if (authResponse.isPresent()) {
            String token = authResponse.get().token();
            AuthResponse response = new AuthResponse(AuthData.fromDomain(token));
            return ResponseEntity.ok().body(response);
        }

        ErrorResponse response = new ErrorResponse(List.of(
            new ErrorResponse.ErrorResponseAttributes(
                HttpStatusCode.valueOf(401).toString(),
                "Invalid credentials",
                "Invalid credentials"
            )
        ));
        return ResponseEntity.status(401).body(response);
    }
}
