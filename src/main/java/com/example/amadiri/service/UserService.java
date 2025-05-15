package com.example.amadiri.service;

import com.example.amadiri.DTO.UserDTO;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.ResourceNotFoundException;
import com.example.amadiri.mapper.UserMapper;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
        
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }
    
    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
    }

    public UserDTO mapToUserResponse(UserDTO currentUser) {
        return currentUser;
    }
}