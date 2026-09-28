package com.example.PetCareBook.DTO;

import java.time.LocalDate;

public record VaccinationResponse(
        Long vaccinationId,
        Long petId,
        String petName,
        Long vaccineTypeId,
        String vaccineName,
        LocalDate vaccinationDate,
        LocalDate nextDueDate,
        String notes
) {}
