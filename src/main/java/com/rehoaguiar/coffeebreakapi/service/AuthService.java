package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.dto.LoginRequest;
import com.rehoaguiar.coffeebreakapi.dto.RegisterRequest;
import com.rehoaguiar.coffeebreakapi.dto.TokenResponse;
import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.exception.EmailAlreadyRegisteredException;
import com.rehoaguiar.coffeebreakapi.exception.InvalidCredentialsException;
import com.rehoaguiar.coffeebreakapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /** Cria a conta vazia: sem xícaras, avaliações ou cafés de exemplo. */
    @Transactional
    public TokenResponse register(RegisterRequest request) {
        String email = normalize(request.getEmail());
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyRegisteredException("E-mail já cadastrado");
        }

        User user = User.builder()
                .name(request.getName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .username(availableUsername(email))
                .build();

        userRepository.save(user);

        return new TokenResponse(jwtService.generateToken(user.getEmail()));
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(normalize(request.getEmail()))
                .orElseThrow(() -> new InvalidCredentialsException("E-mail ou senha inválidos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("E-mail ou senha inválidos");
        }

        return new TokenResponse(jwtService.generateToken(user.getEmail()));
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    /** Sugere um @usuário a partir do e-mail, com número no fim se já estiver em uso. */
    private String availableUsername(String email) {
        String base = email.split("@")[0].replaceAll("[^a-z0-9._]", "");
        if (base.length() < 2) base = "cafe" + base;
        if (base.length() > 24) base = base.substring(0, 24);
        String candidate = base;
        int n = 2;
        while (userRepository.existsByUsernameIgnoreCase(candidate)) {
            candidate = base + n++;
        }
        return candidate;
    }
}
