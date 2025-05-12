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

import java.util.List;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskMapper taskMapper;
    
    @Autowired
    private UserService userService;
    
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDtoList(tasks);
    }
    
    public TaskDTO getTaskById(Long id) {
        Task task = getTaskEntityById(id);
        return taskMapper.toDto(task);
    }
    
    public Task getTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", id));
    }
    
    public TaskDTO createTask(TaskCreationDTO taskCreateDto) {
        // Vérification des permissions (seul un admin peut créer une tâche)
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut créer une tâche");
        }
        
        Task task = taskMapper.toEntity(taskCreateDto);
        task = taskRepository.save(task);
        
        return taskMapper.toDto(task);
    }
    
    public TaskDTO updateTask(Long id, TaskCreationDTO taskUpdateDto) {
        // Vérification des permissions (seul un admin peut modifier une tâche)
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut modifier une tâche");
        }
        
        Task task = getTaskEntityById(id);
        
        // Mise à jour des champs
        task.setTitle(taskUpdateDto.getTitle());
        task.setDescription(taskUpdateDto.getDescription());
        task.setLocation(taskUpdateDto.getLocation());
        task.setSalaire(taskUpdateDto.getSalaire());
        
        task = taskRepository.save(task);
        
        return taskMapper.toDto(task);
    }
    
    public void deleteTask(Long id) {
        // Vérification des permissions (seul un admin peut supprimer une tâche)
        if (!userService.isCurrentUserAdmin()) {
            throw new UnauthorizedException("Seul un administrateur peut supprimer une tâche");
        }
        
        // Vérification de l'existence de la tâche
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task", "id", id);
        }
        
        taskRepository.deleteById(id);
    }
    
    public List<TaskDTO> searchTasksByTitle(String title) {
        List<Task> tasks = taskRepository.findByTitleContainingIgnoreCase(title);
        return taskMapper.toDtoList(tasks);
    }
    
    public List<TaskDTO> searchTasksByLocation(String location) {
        List<Task> tasks = taskRepository.findByLocationContainingIgnoreCase(location);
        return taskMapper.toDtoList(tasks);
    }
    
    public List<TaskDTO> searchTasksBySalaryRange(Double min, Double max) {
        List<Task> tasks = taskRepository.findBySalaireBetween(min, max);
        return taskMapper.toDtoList(tasks);
    }
}