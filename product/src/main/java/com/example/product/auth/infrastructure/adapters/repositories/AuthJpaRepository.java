package com.example.product.auth.infrastructure.adapters.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.product.auth.domain.models.UserLoginResponse;
import com.example.product.management_product.infrastructure.persistence.UserEntity;

@Repository
public interface AuthJpaRepository extends JpaRepository<UserEntity, String> {
    
    @Query("SELECT new com.example.product.auth.domain.models.UserLoginResponse(u.id, u.nickname) " +
           "FROM UserEntity u " +
           "WHERE u.nickname = :nickname AND u.passwd = :passwd")
    Optional<UserLoginResponse> findByNicknameAndPassword(
            @Param("nickname") String nickname,
            @Param("passwd") String passwd
    );
}
