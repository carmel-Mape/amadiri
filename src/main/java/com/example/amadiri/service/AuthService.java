package com.example.amadiri.service;

import com.example.amadiri.DTO.AuthResponse;
import com.example.amadiri.DTO.LoginRequest;
import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.entity.User;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

/**
 * Service gérant l'authentification des utilisateurs.
 * Gère l'inscription et la connexion des utilisateurs.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User(
            request.getNom(),
            request.getPrenom(),
            request.getEmail(),
            passwordEncoder.encode(request.getPassword())
        );
        
        user.addRole("ROLE_USER");
        user = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = tokenProvider.generateToken(authentication);
        
        return new AuthResponse(jwt, user.getId(), user.getEmail(), 
                              user.getNom(), user.getPrenom(), user.isAdmin());
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String jwt = tokenProvider.generateToken(authentication);
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return new AuthResponse(jwt, user.getId(), user.getEmail(), 
                              user.getNom(), user.getPrenom(), user.isAdmin());
    }
}