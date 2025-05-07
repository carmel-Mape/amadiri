package com.example.amadiri.repository;

import com.example.amadiri.entity.Application;
import com.example.amadiri.entity.ApplicationStatus;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByTask(Task task);
    List<Application> findByUserAndStatus(User user, ApplicationStatus status);
    Optional<Application> findByUserAndTask(User user, Task task);
    boolean existsByUserAndTask(User user, Task task);
} 