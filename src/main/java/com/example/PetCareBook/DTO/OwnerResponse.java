package com.example.PetCareBook.DTO;

public record OwnerResponse(
        Long ownerId,
        String ownerName,
        String email,
        String phone
) {}
