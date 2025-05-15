package com.example.amadiri.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité JPA représentant un utilisateur dans le système.
 * Stocke les informations de base de l'utilisateur et son rôle.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Application> applications = new ArrayList<>();

    // Constructeur avec les champs principaux
    public User(String nom, String prenom, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.roles = new ArrayList<>();
    }

    // Méthode utilitaire pour vérifier si l'utilisateur est un administrateur
    public boolean isAdmin() {
        return roles.contains("ROLE_ADMIN");
    }

    // Méthode pour ajouter un rôle
    public void addRole(String role) {
        this.roles.add(role);
    }
}