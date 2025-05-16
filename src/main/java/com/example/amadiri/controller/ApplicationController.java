package com.example.amadiri.controller;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.entity.Application;
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
    public ResponseEntity<?> apply(@RequestBody ApplicationDTO applicationDTO) {
        try {
            Application application = applicationService.apply(applicationDTO.getUserId(), applicationDTO.getTaskId());
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserApplications(@PathVariable Long userId) {
        try {
            List<Application> applications = applicationService.getUserApplications(userId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/task/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTaskApplications(@PathVariable Long taskId) {
        try {
            List<Application> applications = applicationService.getTaskApplications(taskId);
            return ResponseEntity.ok(applications);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status) {
        try {
            Application application = applicationService.updateStatus(id, status);
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}