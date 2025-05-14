package com.example.amadiri.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ApplicationCreateDTO {
    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;

    @NotNull(message = "L'ID de la tâche est obligatoire")
    private Long taskId;
} 