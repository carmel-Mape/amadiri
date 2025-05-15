package com.example.amadiri.controller;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationDTO> apply(
            @RequestParam Long userId,
            @RequestParam Long taskId) {
        return ResponseEntity.ok(applicationService.apply(userId, taskId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationDTO>> getUserApplications(@PathVariable Long userId) {
        return ResponseEntity.ok(applicationService.getUserApplications(userId));
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getTaskApplications(@PathVariable Long taskId) {
        return ResponseEntity.ok(applicationService.getTaskApplications(taskId));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApplicationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        return ResponseEntity.ok(applicationService.updateStatus(id, status));
    }
}