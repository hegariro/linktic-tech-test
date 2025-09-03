package com.example.product.api.v1.controllers;

import com.example.product.api.v1.dto.jsonapi.AuthData;
import com.example.product.api.v1.dto.jsonapi.ErrorResponse;
import com.example.product.api.v1.dto.jsonapi.AuthResponse;
import com.example.product.auth.application.ports.AuthCommand;
import com.example.product.auth.domain.models.Credentials;

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
    public ResponseEntity<?> login(@RequestBody Credentials credentials) {
        Optional<String> token = authCommand.login(credentials.nickname(), credentials.passwd());
        if (token.isPresent()) {
            AuthResponse response = new AuthResponse(AuthData.fromDomain(token.get()));
            return ResponseEntity.ok().body(response);
        }

        ErrorResponse response = new ErrorResponse(List.of(new ErrorResponse.ErrorResponseAttributes(
            HttpStatusCode.valueOf(401).toString(),
            "Invalid credentials",
            "Invalid credentials"
        )));
        return ResponseEntity.status(401).body(response);
    }
}
