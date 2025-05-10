package com.example.amadiri.DTO;

import com.example.amadiri.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour les requêtes de mise à jour du statut d'une candidature.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateRequest {

    @NotNull(message = "Le statut est obligatoire")
    private ApplicationStatus status;
}