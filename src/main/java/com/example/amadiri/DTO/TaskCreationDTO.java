package com.example.amadiri.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreationDTO {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotBlank(message = "La localisation est obligatoire")
    private String location;
    
    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;
} 