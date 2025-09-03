package com.example.product.auth.infrastructure.adapters.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.product.auth.domain.repositories.AuthRepository;
import com.example.product.auth.domain.models.Credentials;
import com.example.product.auth.domain.models.UserLoginResponse;
import com.example.product.shared.security.JwtUtil;

@Component
public class JpaAuthRepositoryAdapter implements AuthRepository {

    private final JwtUtil jwtUtil;
    private final AuthJpaRepository jpaRepository;

    public JpaAuthRepositoryAdapter(JwtUtil jwtUtil, AuthJpaRepository jpaRepository) {
        this.jwtUtil = jwtUtil;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<String> validateCredentials(Credentials credentials) {
        Optional<UserLoginResponse> user = jpaRepository.findByNicknameAndPassword(credentials.nickname(), credentials.passwd());
        if (user.isPresent()) {
            String token = jwtUtil.generateToken(user.get().id(), user.get().nickname());
            return Optional.of(token);
        }
        return Optional.empty();
    }
}
