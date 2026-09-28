package com.example.PetCareBook.Repository;

import com.example.PetCareBook.Model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Owner> findByEmailIgnoreCase(String email);

    boolean existsByUserUserIdAndEmailIgnoreCase(Long userId, String email);
    Optional<Owner> findByOwnerIdAndUserUserId(Long ownerId, Long userId);
    Optional<Owner> findByUserUserIdAndEmailIgnoreCase(Long userId, String email);
    List<Owner> findByUserUserIdOrderByOwnerIdAsc(Long userId);
}
