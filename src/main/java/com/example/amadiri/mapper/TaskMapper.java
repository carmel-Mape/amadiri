package com.example.amadiri.mapper;

import com.example.amadiri.DTO.TaskCreationDTO;
import com.example.amadiri.DTO.TaskDTO;
import com.example.amadiri.entity.Task;
import com.example.amadiri.entity.TaskStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    
    public Task toEntity(TaskCreationDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setLocation(dto.getLocation());
        task.setSalaire(dto.getSalaire());
        task.setStatus(TaskStatus.EN_ATTENTE);
        return task;
    }
    
    public TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setLocation(task.getLocation());
        dto.setSalaire(task.getSalaire());
        dto.setDatePosted(task.getDatePosted());
        dto.setStatus(task.getStatus());
        return dto;
    }
    
    public List<TaskDTO> toDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}