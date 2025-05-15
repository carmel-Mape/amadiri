package com.example.amadiri.DTO;

import com.example.amadiri.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
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

    public ApplicationDTO(Long taskId) {
        this.taskId = taskId;
        this.dateApplied = LocalDateTime.now();
        this.status = ApplicationStatus.EN_ATTENTE;
    }

    public ApplicationDTO(Long id, Long userId, String userName, Long taskId, String taskTitle, 
                         LocalDateTime dateApplied, ApplicationStatus status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.taskId = taskId;
        this.taskTitle = taskTitle;
        this.dateApplied = dateApplied;
        this.status = status;
    }
}