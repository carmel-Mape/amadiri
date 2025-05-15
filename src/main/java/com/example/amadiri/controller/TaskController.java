package com.example.amadiri.controller;

import com.example.amadiri.DTO.TaskDTO;
import com.example.amadiri.DTO.TaskCreationDTO;
import com.example.amadiri.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des tâches.
 * Fournit les endpoints pour créer, lire, modifier et supprimer des tâches.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Liste toutes les tâches disponibles.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    /**
     * Récupère les détails d'une tâche spécifique.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    /**
     * Crée une nouvelle tâche.
     * Accessible uniquement aux administrateurs.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskCreationDTO taskCreateDto) {
        return ResponseEntity.ok(taskService.createTask(taskCreateDto));
    }

    /**
     * Met à jour une tâche existante.
     * Accessible uniquement aux administrateurs.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskCreationDTO taskUpdateDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskUpdateDto));
    }

    /**
     * Supprime une tâche.
     * Accessible uniquement aux administrateurs.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Recherche des tâches par titre.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search")
    public List<TaskDTO> searchTasks(@RequestParam String title) {
        return taskService.searchTasksByTitle(title);
    }

    /**
     * Recherche des tâches par lieu.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search/location")
    public List<TaskDTO> searchTasksByLocation(@RequestParam String location) {
        return taskService.searchTasksByLocation(location);
    }

    /**
     * Recherche des tâches par plage de salaire.
     * Accessible à tous les utilisateurs.
     */
    @GetMapping("/search/salary")
    public List<TaskDTO> searchTasksBySalaryRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return taskService.searchTasksBySalaryRange(min, max);
    }
}