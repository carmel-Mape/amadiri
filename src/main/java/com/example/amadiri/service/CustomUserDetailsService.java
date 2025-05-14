package com.example.amadiri.service;

import com.example.amadiri.entity.User;
import com.example.amadiri.repository.UserRepository;
import com.example.amadiri.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Service qui charge les détails d'un utilisateur pour Spring Security.
 * Implémente l'interface UserDetailsService requise pour l'authentification.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son email pour l'authentification.
     * 
     * @param email L'email de l'utilisateur à charger
     * @return Les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        // Convertit les rôles de l'utilisateur en autorités Spring Security
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        // Retourne un objet UserDetails de Spring Security
        return UserDetailsImpl.build(user);

    }
}