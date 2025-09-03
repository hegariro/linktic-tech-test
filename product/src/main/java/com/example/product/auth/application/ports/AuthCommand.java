package com.example.product.auth.application.ports;

import java.util.Optional;

public interface AuthCommand {
    Optional<String> login(String nickname, String password);
}
