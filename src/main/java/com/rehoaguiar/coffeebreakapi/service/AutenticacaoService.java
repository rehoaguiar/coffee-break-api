package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import com.rehoaguiar.coffeebreakapi.dto.TokenResponse;
import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public TokenResponse register(RegisterRequest request) {
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new TokenResponse(token);
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new TokenResponse(token);
    }
}
