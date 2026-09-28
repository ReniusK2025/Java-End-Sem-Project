package com.example.PetCareBook.Repository;

import com.example.PetCareBook.Model.VaccineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaccineTypeRepository extends JpaRepository<VaccineType, Long> {
    boolean existsByVaccineNameIgnoreCase(String vaccineName);
    Optional<VaccineType> findByVaccineNameIgnoreCase(String vaccineName);

    boolean existsByUserUserIdAndVaccineNameIgnoreCase(Long userId, String vaccineName);
    Optional<VaccineType> findByVaccineTypeIdAndUserUserId(Long id, Long userId);
    Optional<VaccineType> findByUserUserIdAndVaccineNameIgnoreCase(Long userId, String vaccineName);
    List<VaccineType> findByUserUserIdOrderByVaccineTypeIdAsc(Long userId);
}
