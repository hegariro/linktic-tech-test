package com.example.product.auth.application.usecases;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.product.auth.application.ports.AuthCommand;
import com.example.product.auth.domain.repositories.AuthRepository;
import com.example.product.auth.domain.models.Credentials;

@Component
public class AuthUseCase implements AuthCommand {

    private final AuthRepository authRepository;

    public AuthUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public Optional<String> login(String nickname, String password) {
        Optional<String> response = authRepository.validateCredentials(new Credentials(nickname, password));
        return response;
    }
}
