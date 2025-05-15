package com.example.amadiri.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

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
    private List<String> roles;
    private boolean isAdmin;
}