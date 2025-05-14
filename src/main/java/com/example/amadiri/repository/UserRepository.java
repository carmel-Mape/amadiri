package com.example.amadiri.repository;

import com.example.amadiri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository pour gérer les opérations de base de données liées aux utilisateurs.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Recherche un utilisateur par son email.
     * 
     * @param email Email de l'utilisateur à rechercher
     * @return Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Vérifie si un utilisateur existe déjà avec cet email.
     * 
     * @param email Email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    boolean existsByEmail(String email);
}