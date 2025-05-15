package com.example.amadiri.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité JPA représentant un utilisateur dans le système.
 * Stocke les informations de base de l'utilisateur, son rôle et sa date de création.
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

    /**
     * Date de création du compte utilisateur.
     * Cette date est automatiquement définie lors de la création et ne peut pas être modifiée.
     */
    @Column(name = "date_created", nullable = false, updatable = false)
    private LocalDateTime dateCreated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    // Constructeur avec les champs principaux
    public User(String nom, String prenom, String email, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>();
    }

    // Méthode utilitaire pour vérifier si l'utilisateur est un administrateur
    public boolean isAdmin() {
        return roles.contains(Role.ROLE_ADMIN);
    }

    // Méthode pour ajouter un rôle
    public void addRole(Role role) {
        this.roles.add(role);
    }

    /**
     * Méthode appelée automatiquement avant la persistance de l'entité.
     * Initialise la date de création si elle n'est pas déjà définie.
     */
    @PrePersist
    protected void onCreate() {
        if (dateCreated == null) {
            dateCreated = LocalDateTime.now();
        }
    }
}