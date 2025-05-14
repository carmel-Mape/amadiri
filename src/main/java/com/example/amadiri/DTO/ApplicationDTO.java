package com.example.amadiri.DTO;

import com.example.amadiri.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO pour les informations d'une candidature.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDTO {

    private Long id;
    
    private Long userId;
    private String userName;  // Nom complet de l'utilisateur (nom + prénom) pour l'affichage
    
    @NotNull(message = "L'ID de la tâche est obligatoire")
    private Long taskId;
    private String taskTitle;  // Titre de la tâche pour l'affichage
    
    private LocalDateTime dateApplied;
    private ApplicationStatus status;

    // Constructeur minimal pour la création d'une candidature
    public ApplicationDTO(Long taskId) {
        this.taskId = taskId;
   
    }

    
}