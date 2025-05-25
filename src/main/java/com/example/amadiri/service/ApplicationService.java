package com.example.amadiri.service;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.DTO.StatusUpdateRequest;
import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.BadRequestException;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.exception.UnauthorizedException;
import com.example.amadiri.mapper.ApplicationMapper;
import com.example.amadiri.repository.ApplicationRepository;
import com.example.amadiri.repository.TaskRepository;
import com.example.amadiri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ApplicationMapper applicationMapper;
    
    @Transactional
    public Application apply(Long userId, Long taskId) {
        try {
            logger.info("Tentative de création d'une candidature pour l'utilisateur {} et la tâche {}", userId, taskId);
            
            // Vérifier que l'utilisateur existe
            User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("Utilisateur non trouvé avec l'ID: {}", userId);
                    return new ResourceNotFoundException("Utilisateur", "id", userId);
                });
                
            // Vérifier que la tâche existe
            Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    logger.error("Tâche non trouvée avec l'ID: {}", taskId);
                    return new ResourceNotFoundException("Tâche", "id", taskId);
                });

            // Vérifier que l'utilisateur n'a pas déjà postulé
            if (applicationRepository.existsByUserAndTask(user, task)) {
                logger.warn("Tentative de double candidature pour l'utilisateur {} et la tâche {}", userId, taskId);
                throw new BadRequestException("Vous avez déjà postulé à cette tâche");
            }

            Application application = new Application(user, task);
            Application savedApplication = applicationRepository.save(application);
            logger.info("Candidature créée avec succès avec l'ID: {}", savedApplication.getId());
            return savedApplication;
            
        } catch (ResourceNotFoundException | BadRequestException e) {
            logger.error("Erreur lors de la création de la candidature: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de la candidature: {}", e.getMessage(), e);
            throw new RuntimeException("Une erreur inattendue s'est produite lors de la création de la candidature");
        }
    }
    
    public List<Application> getUserApplications(Long userId) {
        try {
            logger.info("Récupération des candidatures pour l'utilisateur {}", userId);
            
            if (!userRepository.existsById(userId)) {
                logger.error("Utilisateur non trouvé avec l'ID: {}", userId);
                throw new ResourceNotFoundException("Utilisateur", "id", userId);
            }
            
            List<Application> applications = applicationRepository.findByUserId(userId);
            logger.info("{} candidatures trouvées pour l'utilisateur {}", applications.size(), userId);
            return applications;
            
        } catch (ResourceNotFoundException e) {
            logger.error("Erreur lors de la récupération des candidatures: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des candidatures: {}", e.getMessage(), e);
            throw new RuntimeException("Une erreur inattendue s'est produite lors de la récupération des candidatures");
        }
    }
    
    public List<Application> getTaskApplications(Long taskId) {
        try {
            logger.info("Récupération des candidatures pour la tâche {}", taskId);
            
            if (!taskRepository.existsById(taskId)) {
                logger.error("Tâche non trouvée avec l'ID: {}", taskId);
                throw new ResourceNotFoundException("Tâche", "id", taskId);
            }
            
            List<Application> applications = applicationRepository.findByTaskId(taskId);
            logger.info("{} candidatures trouvées pour la tâche {}", applications.size(), taskId);
            return applications;
            
        } catch (ResourceNotFoundException e) {
            logger.error("Erreur lors de la récupération des candidatures: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des candidatures: {}", e.getMessage(), e);
            throw new RuntimeException("Une erreur inattendue s'est produite lors de la récupération des candidatures");
        }
    }
    
    @Transactional
    public Application updateStatus(Long applicationId, ApplicationStatus newStatus) {
        try {
            logger.info("Mise à jour du statut de la candidature {} vers {}", applicationId, newStatus);
            
            Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("Candidature non trouvée avec l'ID: {}", applicationId);
                    return new ResourceNotFoundException("Candidature", "id", applicationId);
                });
                
            application.setStatus(newStatus);
            Application updatedApplication = applicationRepository.save(application);
            logger.info("Statut de la candidature {} mis à jour avec succès", applicationId);
            return updatedApplication;
            
        } catch (ResourceNotFoundException e) {
            logger.error("Erreur lors de la mise à jour du statut: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour du statut: {}", e.getMessage(), e);
            throw new RuntimeException("Une erreur inattendue s'est produite lors de la mise à jour du statut");
        }
    }
    
    public ApplicationDTO getApplicationById(Long id) {
        try {
            logger.info("Récupération de la candidature avec l'ID: {}", id);
            
            Application application = applicationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Candidature non trouvée avec l'ID: {}", id);
                    return new ResourceNotFoundException("Application", "id", id);
                });
            
            // Vérification des permissions
            Long currentUserId = userService.getCurrentUserId();
            boolean isAdmin = userService.isCurrentUserAdmin();
            
            if (!application.getUser().getId().equals(currentUserId) && !isAdmin) {
                logger.warn("Tentative d'accès non autorisé à la candidature {} par l'utilisateur {}", id, currentUserId);
                throw new UnauthorizedException("Vous n'êtes pas autorisé à voir cette candidature");
            }
            
            ApplicationDTO dto = applicationMapper.toDto(application);
            logger.info("Candidature {} récupérée avec succès", id);
            return dto;
            
        } catch (ResourceNotFoundException | UnauthorizedException e) {
            logger.error("Erreur lors de la récupération de la candidature: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération de la candidature: {}", e.getMessage(), e);
            throw new RuntimeException("Une erreur inattendue s'est produite lors de la récupération de la candidature");
        }
    }
}
