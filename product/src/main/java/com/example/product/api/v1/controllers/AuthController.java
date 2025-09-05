package com.example.product.api.v1.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.api.v1.dto.jsonapi.AuthData;
import com.example.product.api.v1.dto.jsonapi.AuthResponse;
import com.example.product.api.v1.dto.jsonapi.ErrorResponse;
import com.example.product.auth.application.ports.AuthCommand;
import com.example.product.auth.domain.models.Credentials;

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
