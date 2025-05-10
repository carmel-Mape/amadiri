package com.example.amadiri.DTO;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class TaskCreateDto {
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    @NotBlank(message = "Le lieu est obligatoire")
    private String location;
    
    @NotNull(message = "Le salaire est obligatoire")
    @Min(value = 0, message = "Le salaire doit être positif")
    private Double salaire;
}