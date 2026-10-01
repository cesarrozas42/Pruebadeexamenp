package com.example.prueba.auth.application;

import com.example.prueba.auth.dto.LoginRequest;
import com.example.prueba.common.UnauthorizedException;
import com.example.prueba.security.JwtService;
import com.example.prueba.user.domain.User;
import com.example.prueba.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        // emails are stored lowercased on register
        String email = request.email().trim().toLowerCase();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Unknown email " + email));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UnauthorizedException("Incorrect password");
        }
        return jwtService.generateToken(user);
    }
}
