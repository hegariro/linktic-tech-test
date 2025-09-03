package com.example.inventory.auth.infrastructure.adapters.repositories;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.inventory.auth.domain.repositories.AuthRepository;
import com.example.inventory.auth.domain.models.User;
import com.example.inventory.shared.persistence.entities.UserEntity;
import com.example.inventory.shared.security.JwtUtil;

@Component
public class JpaAuthRepositoryAdapter implements AuthRepository {

    private final JwtUtil jwtUtil;
    private final AuthJpaRepository jpaRepository;

    public JpaAuthRepositoryAdapter(JwtUtil jwtUtil, AuthJpaRepository jpaRepository) {
        this.jwtUtil = jwtUtil;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> validateCredentials(String nickname, String passwd) {
        Optional<UserEntity> response = jpaRepository.findByNicknameAndPassword(nickname, passwd);
        if (response.isPresent()) {
            UserEntity user = response.get();
            String token = jwtUtil.generateToken(user.getId(), user.getNickname());
            return Optional.of(new User(
                user.getId(), user.getNickname(), user.getName(), token
            ));
        }
        return Optional.empty();
    }
}
