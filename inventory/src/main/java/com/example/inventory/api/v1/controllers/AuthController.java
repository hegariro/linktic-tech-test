package com.example.inventory.api.v1.controllers;

// imports
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.api.v1.dto.AuthData;
import com.example.inventory.api.v1.dto.AuthResponse;
import com.example.inventory.api.v1.dto.CredentialsRequest;
import com.example.inventory.api.v1.dto.ErrorResponse;
import com.example.inventory.api.v1.dto.UserLoginResponse;
import com.example.inventory.auth.application.ports.AuthCommand;
import com.zaxxer.hikari.util.Credentials;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Autenticación", description = "Operaciones de autenticación para la API")
public class AuthController {
    
    private final AuthCommand authCommand;

    public AuthController(AuthCommand authCommand) {
        this.authCommand = authCommand;
    }


    // OpenAPI Docs
    @Operation(
        summary = "Autentica a un usuario y genera un token JWT",
        description = "Valida las credenciales del usuario y, si son válidas, devuelve un token JWT para acceder a los recursos protegidos."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Credenciales del usuario",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Credentials.class)
        )
    )
    @ApiResponse(
        responseCode = "200",
        description = "Autenticación exitosa",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = AuthResponse.class)
        )
    )
    @ApiResponse(
        responseCode = "401",
        description = "Credenciales inválidas",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class)
        )
    )
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
