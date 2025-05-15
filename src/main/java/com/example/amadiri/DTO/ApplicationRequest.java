package com.example.amadiri.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    private Long taskId;
    private String message;
} 