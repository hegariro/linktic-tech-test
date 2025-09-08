package com.example.inventory.api.v1.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("AuthResponse DTO Tests")
class AuthResponseTest {

    @Test
    @DisplayName("Should create AuthResponse with a valid AuthData object")
    void shouldCreateAuthResponseWithValidData() {
        // Given
        String testToken = "test_token_123";
        JsonApiData data = AuthData.fromDomain(testToken);

        // When
        AuthResponse authResponse = new AuthResponse(data);

        // Then
        assertNotNull(authResponse, "The AuthResponse instance should not be null");
        assertNotNull(authResponse.data(), "The data field of AuthResponse should not be null");
        assertEquals(data, authResponse.data(), "The AuthData object should be correctly set");
        assertEquals(testToken, authResponse.data().attributes().token(), "The token in the nested AuthData should be correct");
    }
}

