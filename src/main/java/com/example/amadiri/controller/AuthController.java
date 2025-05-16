package com.example.amadiri.controller;

import com.example.amadiri.DTO.AuthResponse;
import com.example.amadiri.DTO.LoginRequest;
import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    /**
     * Endpoint d'inscription d'un nouvel utilisateur.
     * 
     * @param request DTO contenant les informations d'inscription
     * @return ResponseEntity contenant le token JWT si l'inscription réussit
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Tentative d'inscription pour l'email: {}", request.getEmail());
        try {
            AuthResponse response = authService.register(request);
            logger.info("Inscription réussie pour l'email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Échec de l'inscription pour l'email: {}, erreur: {}", 
                request.getEmail(), e.getMessage());
            throw e;
        }
    }

    /**
     * Endpoint de connexion d'un utilisateur.
     * 
     * @param request DTO contenant les informations de connexion
     * @return ResponseEntity contenant le token JWT si la connexion réussit
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Tentative de connexion pour l'email: {}", request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            logger.info("Connexion réussie pour l'email: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Échec de la connexion pour l'email: {}, erreur: {}", 
                request.getEmail(), e.getMessage());
            throw e;
        }
    }
}