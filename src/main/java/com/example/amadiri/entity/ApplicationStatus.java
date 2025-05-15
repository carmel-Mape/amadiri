package com.example.amadiri.entity;

/**
 * Énumération des différents statuts possibles pour une candidature.
 */
public enum ApplicationStatus {
    EN_ATTENTE,   // La candidature vient d'être soumise et n'a pas encore été traitée
    ACCEPTE,      // La candidature a été acceptée
    REFUSE        // La candidature a été refusée
}