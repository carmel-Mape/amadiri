package com.example.amadiri.repository;

import com.example.amadiri.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByLocationContainingIgnoreCase(String location);
    List<Task> findBySalaireBetween(Double min, Double max);
}
