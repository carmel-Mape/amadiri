package com.example.amadiri.service;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.DTO.StatusUpdateRequest;
import com.example.amadiri.DTO.ApplicationCreateDTO;
import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.BadRequestException;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.exception.UnauthorizedException;
import com.example.amadiri.mapper.ApplicationMapper;
import com.example.amadiri.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ApplicationMapper applicationMapper;
    
    public ApplicationDTO applyForTask(ApplicationCreateDTO applicationCreateDto) {
        Long userId = userService.getCurrentUserId();
        Long taskId = applicationCreateDto.getTaskId();
        
        // Vérification si l'utilisateur a déjà postulé à cette tâche
        Optional<Application> existingApplication = applicationRepository.findByUserIdAndTaskId(userId, taskId);
        if (existingApplication.isPresent()) {
            throw new BadRequestException("Vous avez déjà postulé à cette tâche");
        }
        
        // Récupération de l'utilisateur et de la tâche
        User user = userService.getUserEntityById(userId);
        Task task = taskService.getTaskEntityById(taskId);
        
        // Création de la candidature
        Application application = new Application();
        application.setUser(user);
        application.setTask(task);
        application.setDateApplied(LocalDateTime.now());
        application.setStatus(Application.ApplicationStatus.EN_ATTENTE);
        
        application = applicationRepository.save(application);
        
        return applicationMapper.toDto(application);
    }
    
    public List<ApplicationDTO> getUserApplications(Long userId) {
        // Vérification des permissions (un utilisateur ne peut voir que ses propres candidatures)
        if (!userService.getCurrentUserId().equals(userId) && !userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à voir les candidatures de cet utilisateur");
        }
        
        List<Application> applications = applicationRepository.findByUserId(userId);
        return applicationMapper.toDtoList(applications);
    }
    
    public List<ApplicationDTO> getTaskApplications(Long taskId) {
        // Vérification des permissions (seul un admin peut voir toutes les candidatures pour une tâche)
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut voir toutes les candidatures pour une tâche");
        }
        
        List<Application> applications = applicationRepository.findByTaskId(taskId);
        return applicationMapper.toDtoList(applications);
    }
    
    public ApplicationDTO updateApplicationStatus(Long applicationId, StatusUpdateRequest statusUpdateDto) {
        // Vérification des permissions (seul un admin peut mettre à jour le statut d'une candidature)
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut mettre à jour le statut d'une candidature");
        }
        
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
        
        application.setStatus(statusUpdateDto.getStatus());
        application = applicationRepository.save(application);
        
        return applicationMapper.toDto(application);
    }
    
    public ApplicationDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
        
        // Vérification des permissions (un utilisateur ne peut voir que ses propres candidatures)
        if (!application.getUser().getId().equals(userService.getCurrentUserId()) && !userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à voir cette candidature");
        }
        
        return applicationMapper.toDto(application);
    }
}
