package com.example.inventory.management_product.infrastructure.mapper;

import com.example.inventory.management_product.infrastructure.dto.LoginResponse;

public class LoginMapper {
    public static String toToken(LoginResponse response) {
        return response != null && response.getData() != null 
               ? response.getData().getToken()
               : null;
    }
}
