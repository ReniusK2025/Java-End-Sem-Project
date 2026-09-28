package com.example.PetCareBook.DTO;

public record VaccineTypeResponse(
        Long vaccineTypeId,
        String vaccineName,
        String description,
        Integer intervalDays
) {}
