package com.example.amadiri.DTO;

import com.example.amadiri.entity.ApplicationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO pour les informations d'une candidature.
 */
@Data
@NoArgsConstructor
public class ApplicationDTO {

    private Long id;
    
    private Long userId;
    private String userName;  // Nom complet de l'utilisateur (nom + prénom) pour l'affichage
    
    private Long taskId;
    private String taskTitle;  // Titre de la tâche pour l'affichage
    
    private LocalDateTime dateApplied;
    private ApplicationStatus status;

    // Constructor for creating a new application
    public ApplicationDTO(Long userId, Long taskId) {
        this.userId = userId;
        this.taskId = taskId;
        this.dateApplied = LocalDateTime.now();
        this.status = ApplicationStatus.EN_ATTENTE;
    }

    // Constructor for full DTO
    public ApplicationDTO(Long id, Long userId, Long taskId, String userName, 
                        String taskTitle, LocalDateTime dateApplied, ApplicationStatus status) {
        this.id = id;
        this.userId = userId;
        this.taskId = taskId;
        this.userName = userName;
        this.taskTitle = taskTitle;
        this.dateApplied = dateApplied;
        this.status = status;
    }
}