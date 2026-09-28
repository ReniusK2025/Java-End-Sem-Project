package com.example.PetCareBook.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record VaccinationRequest(
        @NotNull(message = "Vaccination date is required")
        @PastOrPresent(message = "Vaccination date cannot be in the future")
        LocalDate vaccinationDate,

        @NotNull(message = "Vaccine type ID is required")
        @Positive(message = "Vaccine type ID must be positive")
        Long vaccineTypeId,

        @Size(max = 255, message = "Notes must not exceed 255 characters")
        String notes
) {}
