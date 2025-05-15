package com.example.amadiri.controller;

import com.example.amadiri.DTO.TaskDTO;
import com.example.amadiri.DTO.TaskCreationDTO;
import com.example.amadiri.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Obtenir toutes les tâches
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Obtenir une tâche par son ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // Créer une nouvelle tâche (admin uniquement)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskCreationDTO taskRequest) {
        TaskDTO createdTask = taskService.createTask(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Mettre à jour une tâche (admin uniquement)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskCreationDTO taskRequest) {
        TaskDTO updatedTask = taskService.updateTask(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    // Supprimer une tâche (admin uniquement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}