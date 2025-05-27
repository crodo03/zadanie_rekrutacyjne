package com.example.library;

import com.example.library.dto.AuthRequest;
import com.example.library.dto.AuthResponse;
import com.example.library.dto.RegisterRequest;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.JwtService;
import com.example.library.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Test
    void testRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest(
                "username1",
                "email@test.com",
                "password1"
        );

        AuthResponse authResponse = authService.register(registerRequest);
        User user = userRepository.findByUsername(registerRequest.getUsername()).orElseThrow();
        String token = authResponse.getToken();
        String extractedUsername = jwtService.extractUsername(token);

        assertAll(
                () -> assertNotEquals(user.getPassword(), registerRequest.getPassword()),
                () -> assertNotNull(token),
                () -> assertEquals(user.getUsername(), extractedUsername),
                () -> assertEquals(user.getEmail(), registerRequest.getEmail()),
                () -> assertTrue(jwtService.isTokenValid(token, user))
        );
    }

    @Test
    void testLoginUser() {
        RegisterRequest registerRequest = new RegisterRequest(
                "username2",
                "email@test2.com",
                "password2"
        );

        authService.register(registerRequest);

        AuthRequest authRequest = new AuthRequest(
                "username2",
                "password2"
        );

        AuthResponse authResponse = authService.authenticate(authRequest);

        String token = authResponse.getToken();
        String extractedUsername = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(authRequest.getUsername()).orElseThrow();

        assertAll(
                () -> assertNotNull(token),
                () -> assertTrue(jwtService.isTokenValid(token, user)),
                () -> assertEquals(authRequest.getUsername(), extractedUsername)
        );
    }
}
