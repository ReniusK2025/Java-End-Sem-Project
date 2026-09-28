package com.example.PetCareBook.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record PetRequest(
        @NotBlank(message = "Pet name is required")
        String petName,

        @NotBlank(message = "Species is required")
        String species,

        @NotBlank(message = "Breed is required")
        String breed,

        @NotNull(message = "Date of birth is required")
        @PastOrPresent(message = "Date of birth cannot be in the future")
        LocalDate dateOfBirth,

        @NotNull(message = "Owner ID is required")
        @Positive(message = "Owner ID must be positive")
        Long ownerId
) {}
