package com.example.amadiri.controller;

import com.example.amadiri.DTO.LoginRequest;
import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 * Fournit les endpoints pour l'inscription et la connexion.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Endpoint d'inscription d'un nouvel utilisateur.
     * 
     * @param registerRequest DTO contenant les informations d'inscription
     * @return ResponseEntity contenant le token JWT si l'inscription réussit
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    /**
     * Endpoint de connexion d'un utilisateur.
     * 
     * @param loginRequest DTO contenant les informations de connexion
     * @return ResponseEntity contenant le token JWT si la connexion réussit
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}