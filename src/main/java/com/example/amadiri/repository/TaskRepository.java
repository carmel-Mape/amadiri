package com.example.amadiri.repository;

import com.example.amadiri.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour gérer les opérations de base de données liées aux tâches.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Recherche des tâches dont le titre ou la description contient le terme recherché.
     * 
     * @param keyword Terme à rechercher
     * @return Liste des tâches correspondantes
     */
    List<Task> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String sameKeyword);
    
    /**
     * Recherche des tâches par lieu.
     * 
     * @param location Lieu à rechercher
     * @return Liste des tâches correspondantes
     */
    List<Task> findByLocationContainingIgnoreCase(String location);
    
    /**
     * Recherche des tâches postées après une certaine date.
     * 
     * @param date Date minimum de publication
     * @return Liste des tâches correspondantes
     */
    List<Task> findByDatePostedAfter(LocalDateTime date);
}