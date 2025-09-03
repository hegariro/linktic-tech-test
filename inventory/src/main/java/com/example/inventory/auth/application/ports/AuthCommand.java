package com.example.inventory.auth.application.ports;

import com.example.inventory.api.v1.dto.UserLoginResponse;
import java.util.Optional;

public interface AuthCommand {
    Optional<UserLoginResponse> login(String nickname, String password);
}
