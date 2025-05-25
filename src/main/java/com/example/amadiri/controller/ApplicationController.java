package com.example.amadiri.controller;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.service.ApplicationService;
import com.example.amadiri.service.UserService;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.exception.UnauthorizedException;
import com.example.amadiri.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApplicationController {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
    private final ApplicationService applicationService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> apply(@RequestBody ApplicationDTO applicationDTO) {
        try {
            logger.info("Tentative de création d'une candidature avec les données: {}", applicationDTO);
            
            if (applicationDTO.getTaskId() == null) {
                logger.warn("Tentative de création de candidature sans ID de tâche");
                return ResponseEntity.badRequest().body("L'ID de la tâche est requis");
            }
            
            if (applicationDTO.getUserId() == null) {
                logger.warn("Tentative de création de candidature sans ID d'utilisateur");
                return ResponseEntity.badRequest().body("L'ID de l'utilisateur est requis");
            }
            
            // Vérifier que l'utilisateur actuel correspond à l'ID fourni
            Long currentUserId = userService.getCurrentUserId();
            if (!currentUserId.equals(applicationDTO.getUserId())) {
                logger.warn("Tentative de création de candidature pour un autre utilisateur. ID actuel: {}, ID demandé: {}", 
                    currentUserId, applicationDTO.getUserId());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Vous ne pouvez pas créer une candidature pour un autre utilisateur");
            }
            
            Application application = applicationService.apply(applicationDTO.getUserId(), applicationDTO.getTaskId());
            logger.info("Candidature créée avec succès: {}", application);
            return ResponseEntity.ok(application);
            
        } catch (UnauthorizedException e) {
            logger.error("Erreur d'autorisation lors de la création de la candidature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée lors de la création de la candidature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            logger.error("Requête invalide lors de la création de la candidature: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de la candidature: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la postulation : " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserApplications(@PathVariable Long userId) {
        try {
            logger.info("Récupération des candidatures pour l'utilisateur {}", userId);
            
            // Vérifier que l'utilisateur actuel correspond à l'ID fourni
            Long currentUserId = userService.getCurrentUserId();
            if (!currentUserId.equals(userId) && !userService.isCurrentUserAdmin()) {
                logger.warn("Tentative d'accès non autorisé aux candidatures de l'utilisateur {}", userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Vous n'êtes pas autorisé à voir les candidatures de cet utilisateur");
            }
            
            List<Application> applications = applicationService.getUserApplications(userId);
            logger.info("{} candidatures trouvées pour l'utilisateur {}", applications.size(), userId);
            return ResponseEntity.ok(applications);
            
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée lors de la récupération des candidatures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des candidatures: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la récupération des candidatures : " + e.getMessage());
        }
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTaskApplications(@PathVariable Long taskId) {
        try {
            logger.info("Récupération des candidatures pour la tâche {}", taskId);
            List<Application> applications = applicationService.getTaskApplications(taskId);
            logger.info("{} candidatures trouvées pour la tâche {}", applications.size(), taskId);
            return ResponseEntity.ok(applications);
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée lors de la récupération des candidatures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des candidatures: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la récupération des candidatures : " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        try {
            logger.info("Mise à jour du statut de la candidature {} vers {}", id, status);
            Application application = applicationService.updateStatus(id, status);
            logger.info("Statut de la candidature {} mis à jour avec succès", id);
            return ResponseEntity.ok(application);
        } catch (ResourceNotFoundException e) {
            logger.error("Ressource non trouvée lors de la mise à jour du statut: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour du statut: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Une erreur s'est produite lors de la mise à jour du statut : " + e.getMessage());
        }
    }
}