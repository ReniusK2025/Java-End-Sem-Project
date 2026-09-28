package com.example.PetCareBook.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VaccineTypeRequest(
        @NotBlank(message = "Vaccine name is required")
        @Size(max = 100, message = "Vaccine name must not exceed 100 characters")
        String vaccineName,

        @Size(max = 255, message = "Description must not exceed 255 characters")
        String description,

        @NotNull(message = "Interval days is required")
        @Positive(message = "Interval days must be greater than zero")
        Integer intervalDays
) {}
