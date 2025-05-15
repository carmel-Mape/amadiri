package com.example.amadiri.service;

import com.example.amadiri.DTO.UserDTO;
import com.example.amadiri.entity.User;
import com.example.amadiri.entity.Role;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.mapper.UserMapper;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    private final UserMapper userMapper;
    
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return userMapper.toDto(user);
    }
    
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        
        return userMapper.toDto(user);
    }
    
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }
    
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
    
    public boolean isCurrentUserAdmin() {
        return getCurrentUser().isAdmin();
    }

    public UserDTO mapToUserResponse(UserDTO currentUser) {
        return currentUser;
    }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = getUserEntityById(userId);
        user.addRole(Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}