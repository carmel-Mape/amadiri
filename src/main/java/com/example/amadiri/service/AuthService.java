package com.example.amadiri.service;

import com.example.amadiri.DTO.LoginRequest;
import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.DTO.JwtAuthResponse;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.BadRequestException;
import com.example.amadiri.mapper.UserMapper;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.JwtUtils;
import com.example.amadiri.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    public User register(RegisterRequest registerDto) {
        // Vérification si l'email existe déjà
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new BadRequestException("Erreur: Cet email est déjà utilisé!");
        }
        
        // Création du nouvel utilisateur
        User user = userMapper.toEntity(registerDto);
        
        // Sauvegarde de l'utilisateur dans la base de données
        return userRepository.save(user);
    }
    
    public JwtAuthResponse login(LoginRequest loginDto) {
        // Authentification de l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        
        // Mise à jour du contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Génération du token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // Récupération des détails de l'utilisateur
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Création et renvoi de la réponse JWT
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(jwt);
        response.setUserId(userDetails.getId());
        response.setEmail(userDetails.getUsername());
        response.setRole(userDetails.getAuthorities().iterator().next().getAuthority());
        return response;
    }
}