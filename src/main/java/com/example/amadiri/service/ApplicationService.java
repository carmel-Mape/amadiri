package com.example.amadiri.service;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.repository.ApplicationRepository;
import com.example.amadiri.repository.TaskRepository;
import com.example.amadiri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public ApplicationDTO apply(Long userId, Long taskId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
            
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        if (applicationRepository.existsByUserIdAndTaskId(userId, taskId)) {
            throw new RuntimeException("Vous avez déjà postulé à cette tâche");
        }

        Application application = new Application(user, task);
        application = applicationRepository.save(application);
        
        return convertToDTO(application);
    }
    
    public List<ApplicationDTO> getUserApplications(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        return applicationRepository.findByUserId(userId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<ApplicationDTO> getTaskApplications(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task", "id", taskId);
        }
        return applicationRepository.findByTaskId(taskId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public ApplicationDTO updateStatus(Long applicationId, ApplicationStatus newStatus) {
        Application application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
            
        application.setStatus(newStatus);
        application = applicationRepository.save(application);
        
        return convertToDTO(application);
    }

    private ApplicationDTO convertToDTO(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setUserId(application.getUser().getId());
        dto.setTaskId(application.getTask().getId());
        dto.setUserName(application.getUser().getNom() + " " + application.getUser().getPrenom());
        dto.setTaskTitle(application.getTask().getTitle());
        dto.setDateApplied(application.getDateApplied());
        dto.setStatus(application.getStatus());
        return dto;
    }
}
