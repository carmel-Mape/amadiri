package com.example.amadiri.service;

import com.example.amadiri.DTO.JwtResponse;
import com.example.amadiri.DTO.LoginRequest;
import com.example.amadiri.DTO.RegisterRequest;
import com.example.amadiri.config.JwtTokenProvider;
import com.example.amadiri.entity.Role;
import com.example.amadiri.entity.User;
import com.example.amadiri.exception.BadRequestException;
import com.example.amadiri.mapper.UserMapper;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service gérant l'authentification des utilisateurs.
 * Gère l'inscription et la connexion des utilisateurs.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    /**
     * Inscrit un nouvel utilisateur.
     * 
     * @param registerRequest DTO contenant les informations d'inscription
     * @return ResponseEntity contenant le token JWT
     */
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Cet email est déjà utilisé");
        }

        // Création du nouvel utilisateur
        User user = userMapper.toEntity(registerRequest);
        user = userRepository.save(user);

        // Authentification automatique après inscription
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                registerRequest.getEmail(),
                registerRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getEmail(),
            userDetails.getNom(),
            userDetails.getPrenom(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        ));
    }

    /**
     * Connecte un utilisateur existant.
     * 
     * @param loginRequest DTO contenant les informations de connexion
     * @return ResponseEntity contenant le token JWT
     */
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getEmail(),
            userDetails.getNom(),
            userDetails.getPrenom(),
            userDetails.getAuthorities().iterator().next().getAuthority()
        ));
    }
}