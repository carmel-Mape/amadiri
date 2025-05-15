package com.example.amadiri.controller;

import com.example.amadiri.DTO.ApplicationCreateDTO;
import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.DTO.StatusUpdateRequest;
import com.example.amadiri.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    // Postuler à une tâche
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApplicationDTO> applyForTask(
            @Valid @RequestBody ApplicationCreateDTO applicationRequest
    ) {
        ApplicationDTO application = applicationService.applyForTask(applicationRequest);
        return new ResponseEntity<>(application, HttpStatus.CREATED);
    }

    // Voir ses propres candidatures
    @GetMapping("/user/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ApplicationDTO>> getUserApplications(
            @PathVariable Long userId
    ) {
        List<ApplicationDTO> applications = applicationService.getUserApplications(userId);
        return ResponseEntity.ok(applications);
    }

    // Voir les candidatures pour une tâche (admin uniquement)
    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getTaskApplications(
            @PathVariable Long taskId
    ) {
        List<ApplicationDTO> applications = applicationService.getTaskApplications(taskId);
        return ResponseEntity.ok(applications);
    }

    // Mettre à jour le statut d'une candidature (admin uniquement)
    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody StatusUpdateRequest statusRequest
    ) {
        ApplicationDTO updatedApplication = 
            applicationService.updateApplicationStatus(applicationId, statusRequest);
        return ResponseEntity.ok(updatedApplication);
    }
}