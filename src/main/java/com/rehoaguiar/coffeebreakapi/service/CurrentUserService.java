package com.rehoaguiar.coffeebreakapi.service;

import com.rehoaguiar.coffeebreakapi.entity.User;
import com.rehoaguiar.coffeebreakapi.exception.InvalidCredentialsException;
import com.rehoaguiar.coffeebreakapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Resolve a conta dona do token JWT da requisição. */
@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new InvalidCredentialsException("Sessão expirada. Entre de novo.");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new InvalidCredentialsException("Sessão expirada. Entre de novo."));
    }
}
