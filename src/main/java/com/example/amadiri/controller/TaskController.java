package com.example.amadiri.controller;

import com.example.amadiri.entity.Task;
import com.example.amadiri.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des tâches.
 * Fournit les endpoints pour créer, lire, modifier et supprimer des tâches.
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Liste toutes les tâches disponibles.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Récupère les détails d'une tâche spécifique.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crée une nouvelle tâche.
     * Accessible uniquement aux administrateurs.
     */
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    /**
     * Met à jour une tâche existante.
     * Accessible uniquement aux administrateurs.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        try {
            Task updatedTask = taskService.updateTask(id, taskDetails);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime une tâche.
     * Accessible uniquement aux administrateurs.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recherche des tâches par titre.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String keyword) {
        return taskService.searchTasks(keyword);
    }

    /**
     * Recherche des tâches par lieu.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search/location")
    public List<Task> searchByLocation(@RequestParam String location) {
        return taskService.searchByLocation(location);
    }

    /**
     * Recherche des tâches par plage de salaire.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search/salary")
    public List<Task> searchBySalaryRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return taskService.searchBySalaryRange(min, max);
    }
}