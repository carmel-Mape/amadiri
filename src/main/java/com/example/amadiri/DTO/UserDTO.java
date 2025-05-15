package com.example.amadiri.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO pour les informations d'un utilisateur.
 * Ne contient pas d'informations sensibles comme le mot de passe.
 * Inclut la date de création du compte pour le suivi.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String nom;
    private String prenom;
    private String role;
    
    /**
     * Date de création du compte utilisateur.
     * En lecture seule, ne peut pas être modifiée après la création.
     */
    private LocalDateTime dateCreated;
}