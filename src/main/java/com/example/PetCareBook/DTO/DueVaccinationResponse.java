package com.example.PetCareBook.DTO;

import java.time.LocalDate;

public record DueVaccinationResponse(
        Long vaccinationId,
        Long petId,
        String petName,
        String ownerName,
        String vaccineName,
        LocalDate vaccinationDate,
        LocalDate nextDueDate
) {}
