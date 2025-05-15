package com.example.amadiri.service;

import com.example.amadiri.DTO.TaskDTO;
import com.example.amadiri.DTO.TaskCreationDTO;
import com.example.amadiri.entity.Task;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.exception.UnauthorizedException;
import com.example.amadiri.mapper.TaskMapper;
import com.example.amadiri.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service gérant la logique métier des tâches.
 * Implémente les opérations CRUD et la recherche de tâches.
 */
@Service
@Transactional
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private UserService userService;

    /**
     * Récupère toutes les tâches.
     */
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }

    /**
     * Récupère une tâche par son ID.
     * 
     * @throws ResourceNotFoundException si la tâche n'existe pas
     */
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskEntityById(id);
        return taskMapper.toDto(task);
    }

    /**
     * Récupère l'entité Task par son ID.
     * Méthode utilitaire interne.
     */
    public Task getTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }

    /**
     * Crée une nouvelle tâche.
     * Vérifie que l'utilisateur est un administrateur.
     */
    public TaskDTO createTask(TaskCreationDTO taskCreateDto) {
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut créer une tâche");
        }
        
        Task task = taskMapper.toEntity(taskCreateDto);
        task = taskRepository.save(task);
        
        return taskMapper.toDto(task);
    }

    /**
     * Met à jour une tâche existante.
     * Vérifie que l'utilisateur est un administrateur.
     */
    public TaskDTO updateTask(Long id, TaskCreationDTO taskUpdateDto) {
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut modifier une tâche");
        }
        
        Task task = getTaskEntityById(id);
        
        task.setTitle(taskUpdateDto.getTitle());
        task.setDescription(taskUpdateDto.getDescription());
        task.setLocation(taskUpdateDto.getLocation());
        task.setSalaire(taskUpdateDto.getSalaire());
        
        task = taskRepository.save(task);
        
        return taskMapper.toDto(task);
    }

    /**
     * Supprime une tâche.
     * Vérifie que l'utilisateur est un administrateur.
     */
    public void deleteTask(Long id) {
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut supprimer une tâche");
        }
        
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        
        taskRepository.deleteById(id);
    }

    /**
     * Recherche des tâches par titre.
     */
    public List<TaskDTO> searchTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return taskMapper.toDtoList(tasks);
    }

    /**
     * Recherche des tâches par lieu.
     */
    public List<TaskDTO> searchTasksByLocation(String location) {
        List<Task> tasks = taskRepository.findByLocationContainingIgnoreCase(location);
        return taskMapper.toDtoList(tasks);
    }

    /**
     * Recherche des tâches par plage de salaire.
     */
    public List<TaskDTO> searchTasksBySalaryRange(Double min, Double max) {
        List<Task> tasks = taskRepository.findBySalaireBetween(min, max);
        return taskMapper.toDtoList(tasks);
    }
}