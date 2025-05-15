package com.example.amadiri.DTO;

import lombok.Data;

/**
 * DTO pour la réponse contenant le token JWT et les informations de l'utilisateur.
 */
@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String role;

    public JwtResponse(String token, Long id, String email, String nom, String prenom, String role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }
} 