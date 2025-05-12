package com.example.amadiri.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les réponses d'authentification contenant le token JWT.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long userId;
    private String nom;
    private String prenom;
    private String email;
    private boolean isAdmin;

    public JwtAuthResponse(String token, Long userId, String nom, String prenom, String email, boolean isAdmin) {
        this.token = token;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public JwtAuthResponse(String token, Long userId, String email, boolean isAdmin) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.isAdmin = isAdmin;
    }
}