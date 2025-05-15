package com.example.amadiri.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.example.amadiri.entity.ApplicationStatus;

import com.example.amadiri.entity.Task;


/**
 * Entité JPA représentant une candidature à une tâche.
 * Établit la relation entre un utilisateur et une tâche à laquelle il postule.
 * Stocke également la date de candidature et le statut de la candidature.
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

    /**
     * Date à laquelle la candidature a été soumise.
     * Cette date est automatiquement définie lors de la création et ne peut pas être modifiée.
     */
    @Column(name = "date_applied", nullable = false, updatable = false)
    private LocalDateTime dateApplied;

    /**
     * Statut actuel de la candidature (EN_ATTENTE, ACCEPTE, REFUSE).
     * Par défaut, une nouvelle candidature est en attente.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.EN_ATTENTE;

    /**
     * Constructeur avec les champs principaux.
     * Initialise automatiquement la date de candidature au moment présent.
     */
    public Application(User user, Task task) {
        this.user = user;
        this.task = task;
        this.dateApplied = LocalDateTime.now();
        this.status = ApplicationStatus.EN_ATTENTE;
    }

    /**
     * Méthode appelée automatiquement avant la persistance de l'entité.
     * Assure que la date de candidature et le statut sont correctement initialisés.
     */
    @PrePersist
    protected void onCreate() {
        if (dateApplied == null) {
            dateApplied = LocalDateTime.now();
        }
        if (status == null) {
            status = ApplicationStatus.EN_ATTENTE;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public LocalDateTime getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDateTime dateApplied) {
        this.dateApplied = dateApplied;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}