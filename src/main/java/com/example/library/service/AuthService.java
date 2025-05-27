package com.example.library.service;

import com.example.library.dto.AuthResponse;
import com.example.library.dto.AuthRequest;
import com.example.library.dto.RegisterRequest;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        String requestUsername = request.getUsername();
        String requestEmail = request.getEmail();
        String requestPassword = request.getPassword();

        if(isUsernameInvalid(requestUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid username");
        }

        if(userRepository.existsByUsername(requestUsername)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already in use");
        }

        if(isEmailInvalid(requestEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }

        if(userRepository.existsByEmail(requestEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already in use");
        }

        User user = new User(requestUsername, requestEmail, encoder.encode(requestPassword));
        userRepository.save(user);

        String token = jwtService.generateToken(requestUsername);
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        String username = request.getUsername();
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                username,
                request.getPassword()
            )
        );

        userRepository.findByUsername(username).orElseThrow();
        String token = jwtService.generateToken(username);
        return new AuthResponse(token);
    }

    private boolean isUsernameInvalid(String username) {
        return username == null || !username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private boolean isEmailInvalid(String email) {
        return email == null || !email.matches("^[\\w.-]{2,20}@[a-zA-Z0-9.-]{2,}\\.[a-zA-Z]{2,6}$");
    }
}
