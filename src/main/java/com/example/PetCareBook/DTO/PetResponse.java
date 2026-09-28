package com.example.PetCareBook.DTO;

import java.time.LocalDate;

public record PetResponse(
        Long petId,
        String petName,
        String species,
        String breed,
        LocalDate dateOfBirth,
        Long ownerId,
        String ownerName
) {}
