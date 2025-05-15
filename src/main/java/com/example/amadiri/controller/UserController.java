package com.example.amadiri.controller;

import com.example.amadiri.DTO.UserDTO;
import com.example.amadiri.DTO.ApiResponse;
import com.example.amadiri.entity.User;
import com.example.amadiri.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")  // Garantit que seuls les utilisateurs authentifiés peuvent accéder à ce endpoint
    public ResponseEntity<UserDTO> getCurrentUser() {
        UserDTO currentUser = userService.getCurrentUser();
        UserDTO userResponse = userService.mapToUserResponse(currentUser);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/{userId}/promote-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> promoteToAdmin(@PathVariable Long userId) {
        userService.promoteToAdmin(userId);
        return ResponseEntity.ok(new ApiResponse(true, "Utilisateur promu administrateur avec succès"));
    }
}