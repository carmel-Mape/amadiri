package com.example.amadiri.DTO;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class TaskCreationDTO {
    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotBlank(message = "La localisation est obligatoire")
    private String location;

    @NotNull(message = "Le salaire est obligatoire")
    @Positive(message = "Le salaire doit être positif")
    private Double salaire;
} 