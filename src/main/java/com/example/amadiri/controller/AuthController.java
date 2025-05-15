package com.example.amadiri.controller;

import com.example.amadiri.dto.AuthResponse;
import com.example.amadiri.dto.LoginRequest;
import com.example.amadiri.dto.RegisterRequest;
import com.example.amadiri.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 * Fournit les endpoints pour l'inscription et la connexion.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint d'inscription d'un nouvel utilisateur.
     * 
     * @param request DTO contenant les informations d'inscription
     * @return ResponseEntity contenant le token JWT si l'inscription réussit
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Endpoint de connexion d'un utilisateur.
     * 
     * @param request DTO contenant les informations de connexion
     * @return ResponseEntity contenant le token JWT si la connexion réussit
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}