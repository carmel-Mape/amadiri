
package com.example.amadiri.repository;

import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les opérations de base de données liées aux candidatures.
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
    /**
     * Recherche toutes les candidatures d'un utilisateur.
     * 
     * @param user Utilisateur dont on veut les candidatures
     * @return Liste des candidatures de l'utilisateur
     */
    List<Application> findByUser(User user);
    
    /**
     * Recherche toutes les candidatures pour une tâche spécifique.
     * 
     * @param task Tâche dont on veut les candidatures
     * @return Liste des candidatures pour cette tâche
     */
    List<Application> findByTask(Task task);
    
    /**
     * Recherche toutes les candidatures d'un utilisateur avec un statut spécifique.
     * 
     * @param user Utilisateur concerné
     * @param status Statut recherché
     * @return Liste des candidatures correspondantes
     */
    List<Application> findByUserAndStatus(User user, ApplicationStatus status);
    
    /**
     * Recherche une candidature spécifique par utilisateur et tâche.
     * 
     * @param user Utilisateur concerné
     * @param task Tâche concernée
     * @return Optional contenant la candidature si elle existe
     */
    Optional<Application> findByUserAndTask(User user, Task task);
    
    /**
     * Vérifie si une candidature existe déjà pour cet utilisateur et cette tâche.
     * 
     * @param user Utilisateur concerné
     * @param task Tâche concernée
     * @return true si la candidature existe, false sinon
     */
    boolean existsByUserAndTask(User user, Task task);
}