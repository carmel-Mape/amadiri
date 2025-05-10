package com.example.amadiri.mapper;

import com.example.amadiri.DTO.ApplicationDTO;
import com.example.amadiri.entity.Application;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApplicationMapper {
    
    public ApplicationDTO toDto(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setUserId(application.getUser().getId());
        dto.setUserName(application.getUser().getPrenom() + " " + application.getUser().getNom());
        dto.setTaskId(application.getTask().getId());
        dto.setTaskTitle(application.getTask().getTitle());
        dto.setDateApplied(application.getDateApplied());
        dto.setStatus(application.getStatus());
        return dto;
    }
    
    public List<ApplicationDTO> toDtoList(List<Application> applications) {
        return applications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}