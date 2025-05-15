package com.example.amadiri.service;

import com.example.amadiri.entity.Task;
import com.example.amadiri.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service gérant la logique métier des tâches.
 * Implémente les opérations CRUD et la recherche de tâches.
 */
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Récupère toutes les tâches.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Récupère une tâche par son ID.
     */
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Crée une nouvelle tâche.
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Met à jour une tâche existante.
     */
    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setLocation(taskDetails.getLocation());
        task.setSalaire(taskDetails.getSalaire());
        
        return taskRepository.save(task);
    }

    /**
     * Supprime une tâche.
     */
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.delete(task);
    }

    /**
     * Recherche des tâches par mot-clé.
     */
    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    /**
     * Recherche des tâches par lieu.
     */
    public List<Task> searchByLocation(String location) {
        return taskRepository.findByLocationContainingIgnoreCase(location);
    }

    /**
     * Recherche des tâches par plage de salaire.
     */
    public List<Task> searchBySalaryRange(Double min, Double max) {
        return taskRepository.findBySalaireBetween(min, max);
    }
}