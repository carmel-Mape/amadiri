package com.example.amadiri.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.example.amadiri.entity.Task;


/**
 * Entité JPA représentant une candidature à une tâche.
 * Établit la relation entre un utilisateur et une tâche à laquelle il postule.
 */
@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "task_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "date_applied", nullable = false)
    private LocalDateTime dateApplied;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    // Constructeur avec les champs principaux
    public Application(User user, Task task) {
        this.user = user;
        this.task = task;
        this.dateApplied = LocalDateTime.now();
        this.status = ApplicationStatus.EN_ATTENTE;
    }

    // Méthode appelée avant la persistence pour initialiser les valeurs par défaut
    @PrePersist
    protected void onCreate() {
        if (dateApplied == null) {
            dateApplied = LocalDateTime.now();
        }
        if (status == null) {
            status = ApplicationStatus.EN_ATTENTE;
        }
    }
}