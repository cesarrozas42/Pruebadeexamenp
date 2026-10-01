package com.example.prueba.auth.application;

import com.example.prueba.auth.dto.LoginRequest;
import com.example.prueba.auth.dto.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(new TokenResponse(authService.login(request)));
    }
}
