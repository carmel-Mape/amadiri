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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
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
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
            
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        if (applicationRepository.existsByUserIdAndTaskId(userId, taskId)) {
            throw new RuntimeException("Vous avez déjà postulé à cette tâche");
        }

        Application application = new Application(user, task);
        return applicationRepository.save(application);
    }
    
    public List<Application> getUserApplications(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        return applicationRepository.findByUserId(userId);
    }
    
    public List<Application> getTaskApplications(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RuntimeException("Tâche non trouvée");
        }
        return applicationRepository.findByTaskId(taskId);
    }
    
    @Transactional
    public Application updateStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Candidature non trouvée"));
            
        application.setStatus(newStatus);
        return applicationRepository.save(application);
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
