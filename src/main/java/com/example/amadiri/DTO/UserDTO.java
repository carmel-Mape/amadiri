package com.example.amadiri.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les informations d'un utilisateur.
 * Ne contient pas d'informations sensibles comme le mot de passe.
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
}