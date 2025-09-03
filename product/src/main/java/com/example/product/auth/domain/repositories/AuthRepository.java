package com.example.product.auth.domain.repositories;

import java.util.Optional;
import com.example.product.auth.domain.models.Credentials;

public interface AuthRepository {
    Optional<String> validateCredentials(Credentials credentials);
}
