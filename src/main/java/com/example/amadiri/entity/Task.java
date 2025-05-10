package com.example.amadiri.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Double salaire;

    @Column(name = "date_posted", nullable = false)
    private LocalDateTime datePosted;

    // Constructeur avec les champs principaux
    public Task(String title, String description, String location, Double salaire) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.salaire = salaire;
        this.datePosted = LocalDateTime.now();
    }

    // Méthode appelée avant la persistence pour initialiser la date si nécessaire
    @PrePersist
    protected void onCreate() {
        if (datePosted == null) {
            datePosted = LocalDateTime.now();
        }
    }
}