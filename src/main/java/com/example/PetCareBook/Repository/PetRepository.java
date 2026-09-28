package com.example.PetCareBook.Repository;

import com.example.PetCareBook.Model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByOwnerOwnerId(Long ownerId);
    List<Pet> findByPetNameContainingIgnoreCase(String name);

    List<Pet> findByOwnerUserUserIdOrderByPetIdAsc(Long userId);
    List<Pet> findByOwnerUserUserIdAndPetNameContainingIgnoreCaseOrderByPetIdAsc(Long userId, String name);
    Optional<Pet> findByPetIdAndOwnerUserUserId(Long petId, Long userId);
}
