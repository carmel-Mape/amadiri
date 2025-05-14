package com.example.amadiri.DTO;

import com.example.amadiri.entity.TaskStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO pour les informations d'une tâche.
 * Utilisé à la fois pour les requêtes de création/modification et les réponses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 3, max = 100, message = "Le titre doit contenir entre 3 et 100 caractères")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 2000, message = "La description doit contenir entre 10 et 2000 caractères")
    private String description;

    @NotBlank(message = "Le lieu est obligatoire")
    private String location;

    @NotNull(message = "Le salaire est obligatoire")
    @Min(value = 0, message = "Le salaire doit être positif")
    private Double salaire;

    private LocalDateTime createdAt;
    
    private LocalDateTime dueDate;
    
    private TaskStatus status;
    
    private Long creatorId;

    // Constructeur pour la création sans id ni date
    public TaskDTO(String title, String description, String location, Double salaire) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaire = salaire;
    }
}