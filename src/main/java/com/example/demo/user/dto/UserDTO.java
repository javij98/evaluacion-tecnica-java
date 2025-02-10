package com.example.demo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private LocalDate registrationDate;
}