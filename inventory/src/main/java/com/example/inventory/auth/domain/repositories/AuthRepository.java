package com.example.inventory.auth.domain.repositories;

import java.util.Optional;
import com.example.inventory.auth.domain.models.User;

public interface AuthRepository {
    Optional<User> validateCredentials(String nickname, String passwd);
}
