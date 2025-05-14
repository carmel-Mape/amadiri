package com.example.amadiri.mapper;

import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.DTO.UserDTO;
import com.example.amadiri.entity.User;
import com.example.amadiri.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre l'entité User et les DTOs.
 */
@Component
public class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Transforme un RegisterRequest en entité User.
     */
    public User toEntity(RegisterRequest dto) {
        User user = new User();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.addRole(Role.ROLE_USER);
        return user;
    }

    /**
     * Transforme un User en UserDTO simple.
     */
    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setNom(user.getNom());
        dto.setPrenom(user.getPrenom());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRoles().iterator().next().name());
        return dto;
    }

    /**
     * Transforme un User en réponse pour l'utilisateur authentifié (/users/me).
     */
    public UserDTO mapToUserResponse(User user) {
        // Réutilise la méthode toDto ou ajoute des champs supplémentaires si besoin
        return toDto(user);
    }
}
