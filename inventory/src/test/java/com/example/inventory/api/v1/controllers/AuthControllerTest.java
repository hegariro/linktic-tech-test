package com.example.inventory.api.v1.controllers;

import java.util.Optional;

import static org.hamcrest.Matchers.matchesPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.inventory.api.v1.dto.UserLoginResponse;
import com.example.inventory.auth.application.ports.AuthCommand;

/**
 * Tests unitarios para {@link AuthController}.
 * 
 * <p>Esta clase verifica el comportamiento del controlador de autenticación,
 * incluyendo casos exitosos y de error en el proceso de login.</p>
 * 
 * <p>Utiliza {@code @WebMvcTest} para cargar solo el contexto web necesario
 * y mockea las dependencias externas para aislar la lógica del controlador.</p>
 */
@WebMvcTest(
    controllers = {AuthController.class},
    excludeAutoConfiguration = {SecurityAutoConfiguration.class}
)
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //@Autowired
    //private ObjectMapper objectMapper;
    @MockBean
    private AuthCommand authCommand;

    private static final String BASE_URL = "/v1/auth";
    private static final String LOGIN_ENDPOINT = BASE_URL + "/login";

    /**
     * Configuración inicial para cada test.
     * Prepara los datos comunes y el estado inicial necesario.
     */
    //@BeforeEach
    //void setUp() { }

    /**
     * Verifica que el login exitoso retorna un token JWT válido.
     * 
     * <p><strong>Escenario:</strong> Usuario con credenciales válidas</p>
     * <p><strong>Resultado esperado:</strong> HTTP 200 con token en la respuesta</p>
     */
    @Test
    @DisplayName("Should return JWT token when credentials are valid")
    void shouldReturnJwtTokenWhenCredentialsAreValid() throws Exception {
        // Given
        String nickname = "testuser";
        String password = "testpass";
        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.testtoken";
        String expectedPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        
        String requestBody = String.format("""
            {
                "nickname": "%s",
                "passwd": "%s"
            }
            """, nickname, password);

        UserLoginResponse mockResponse = new UserLoginResponse(expectedToken);
        when(authCommand.login(nickname, password))
            .thenReturn(Optional.of(mockResponse));

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.data.id").value(matchesPattern(expectedPattern)))
          .andExpect(jsonPath("$.data.type").value("auth"))
          .andExpect(jsonPath("$.data.attributes.token").value(expectedToken));
    }

    /**
     * Verifica que las credenciales inválidas retornan error 401.
     * 
     * <p><strong>Escenario:</strong> Usuario con credenciales incorrectas</p>
     * <p><strong>Resultado esperado:</strong> HTTP 401 con mensaje de error</p>
     */
    @Test
    @DisplayName("Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        // Given
        String nickname = "wronguser";
        String password = "wrongpass";
        
        String requestBody = String.format("""
            {
                "nickname": "%s",
                "passwd": "%s"
            }
            """, nickname, password);

        when(authCommand.login(nickname, password))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0].status").value("401 UNAUTHORIZED"))
                .andExpect(jsonPath("$.errors[0].title").value("Invalid credentials"))
                .andExpect(jsonPath("$.errors[0].detail").value("Invalid credentials"));
    }

    /**
     * Verifica que una request con formato JSON inválido retorna error 400.
     * 
     * <p><strong>Escenario:</strong> Request body con JSON malformado</p>
     * <p><strong>Resultado esperado:</strong> HTTP 400 Bad Request</p>
     */
    @Test
    @DisplayName("Should return 400 when request body is invalid JSON")
    void shouldReturn400WhenRequestBodyIsInvalidJson() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verifica que una request sin Content-Type retorna error 415.
     * 
     * <p><strong>Escenario:</strong> Request sin header Content-Type</p>
     * <p><strong>Resultado esperado:</strong> HTTP 415 Unsupported Media Type</p>
     */
    @Test
    @DisplayName("Should return 415 when Content-Type header is missing")
    void shouldReturn415WhenContentTypeHeaderIsMissing() throws Exception {
        // Given
        String requestBody = """
            {
                "nickname": "testuser",
                "passwd": "testpass"
            }
            """;

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .content(requestBody)) // Sin Content-Type
                .andExpect(status().isUnsupportedMediaType());
    }

    /**
     * Verifica que campos faltantes en la request son manejados adecuadamente.
     * 
     * <p><strong>Escenario:</strong> Request sin campos requeridos</p>
     * <p><strong>Resultado esperado:</strong> Manejo apropiado del error</p>
     */
    @Test
    @DisplayName("Should handle missing required fields gracefully")
    void shouldHandleMissingRequiredFields() throws Exception {
        // Given
        String requestBodyMissingPassword = """
            {
                "nickname": "testuser"
            }
            """;

        when(authCommand.login(anyString(), anyString()))
            .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyMissingPassword))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Verifica que el servicio de autenticación lanzando excepción es manejado.
     * 
     * <p><strong>Escenario:</strong> AuthCommand lanza excepción inesperada</p>
     * <p><strong>Resultado esperado:</strong> Error manejado apropiadamente</p>
     */
    @Test
    @DisplayName("Should handle authentication service exceptions")
    void shouldHandleAuthenticationServiceExceptions() throws Exception {
        // Given
        String nickname = "testuser";
        String password = "testpass";
        
        String requestBody = String.format("""
            {
                "nickname": "%s",
                "passwd": "%s"
            }
            """, nickname, password);

        when(authCommand.login(nickname, password))
            .thenThrow(new RuntimeException("Database connection failed"));

        // When & Then
        mockMvc.perform(post(LOGIN_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is5xxServerError());
    }
}
