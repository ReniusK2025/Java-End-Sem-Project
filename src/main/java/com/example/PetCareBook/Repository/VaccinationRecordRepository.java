package com.example.PetCareBook.Repository;

import com.example.PetCareBook.Model.VaccinationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecord, Long> {
    List<VaccinationRecord> findByPetPetIdOrderByVaccinationDateDesc(Long petId);

    List<VaccinationRecord> findByPetOwnerUserUserIdAndNextDueDateBetweenOrderByNextDueDateAsc(
            Long userId, LocalDate from, LocalDate to);

    long countByPetPetId(Long petId);
    long countByVaccineTypeVaccineTypeId(Long vaccineTypeId);

    Optional<VaccinationRecord> findByVaccinationIdAndPetOwnerUserUserId(Long id, Long userId);
}
