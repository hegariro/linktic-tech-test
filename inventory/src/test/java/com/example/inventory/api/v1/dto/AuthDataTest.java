package com.example.inventory.api.v1.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AuthData DTO Tests")
class AuthDataTest {

    @Test
    @DisplayName("Should create AuthData instance with correct token from domain")
    void fromDomain_shouldCreateAuthDataWithCorrectToken() {
        // Given
        String type = "auth";
        String domainToken = "test_token_12345";
        String expectedPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        
        // When
        JsonApiData<AuthData> data = AuthData.fromDomain(domainToken);

        // Then
        assertNotNull(data, "The created AuthData instance should not be null");
        assertTrue(data.id().matches(expectedPattern));
        assertEquals(type, data.type(), "The type in JsonApiData should match the type attribute");
        assertEquals(domainToken, data.attributes().token(), "The token in AuthData should match the domain token");
    }

    @Test
    @DisplayName("Should correctly represent AuthData as a string")
    void toString_shouldReturnCorrectRepresentation() {
        // Given
        String domainToken = "another_test_token";

        // When
        JsonApiData<AuthData> data = AuthData.fromDomain(domainToken);
        String stringRepresentation = data.toString();

        // Then
        // 602eb280-aa8a-4108-95a0-00aab7c16296
        // El formato por defecto de un 'record' es
        // JsonApiData[id=441bc95c-ccb6-4595-960f-946c194eba80, type=auth, attributes=AuthData[token=another_test_token]]
        String uuidRegex = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        String expectedPattern = String.format("JsonApiData\\[id=%s, type=auth, attributes=AuthData\\[token=another_test_token\\]\\]", uuidRegex);

        assertTrue(stringRepresentation.matches(expectedPattern));
    }
}

