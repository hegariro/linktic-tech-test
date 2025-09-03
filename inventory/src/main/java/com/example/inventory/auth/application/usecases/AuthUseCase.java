package com.example.inventory.auth.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.auth.application.ports.AuthCommand;
import com.example.inventory.auth.domain.repositories.AuthRepository;
import com.example.inventory.api.v1.dto.UserLoginResponse;

@Component
public class AuthUseCase implements AuthCommand {

    private final AuthRepository authRepository;

    public AuthUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public Optional<UserLoginResponse> login(String nickname, String password) {
        return authRepository.validateCredentials(nickname, password).map(res -> new UserLoginResponse(res.token()));
    }
}
