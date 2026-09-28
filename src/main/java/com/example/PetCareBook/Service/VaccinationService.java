package com.example.PetCareBook.Service;

import com.example.PetCareBook.DTO.DueVaccinationResponse;
import com.example.PetCareBook.DTO.VaccinationRequest;
import com.example.PetCareBook.DTO.VaccinationResponse;
import com.example.PetCareBook.Exception.BusinessRuleException;
import com.example.PetCareBook.Exception.ResourceNotFoundException;
import com.example.PetCareBook.Model.Pet;
import com.example.PetCareBook.Model.VaccinationRecord;
import com.example.PetCareBook.Model.VaccineType;
import com.example.PetCareBook.Repository.VaccinationRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class VaccinationService {

    private final VaccinationRecordRepository vaccinationRepository;
    private final PetService petService;
    private final VaccineTypeService vaccineTypeService;
    private final CurrentUserService currentUserService;

    @Autowired
    public VaccinationService(
            VaccinationRecordRepository vaccinationRepository,
            PetService petService,
            VaccineTypeService vaccineTypeService,
            CurrentUserService currentUserService) {
        this.vaccinationRepository = vaccinationRepository;
        this.petService = petService;
        this.vaccineTypeService = vaccineTypeService;
        this.currentUserService = currentUserService;
    }


    public VaccinationService(
            VaccinationRecordRepository vaccinationRepository,
            PetService petService,
            VaccineTypeService vaccineTypeService) {
        this(vaccinationRepository, petService, vaccineTypeService, null);
    }
    @Transactional
    public VaccinationResponse record(Long petId, VaccinationRequest request) {
        Pet pet = petService.getPet(petId);
        VaccineType vaccineType = vaccineTypeService.getType(request.vaccineTypeId());
        validateVaccinationDate(pet, request.vaccinationDate());

        VaccinationRecord record = new VaccinationRecord();
        record.setPet(pet);
        record.setVaccineType(vaccineType);
        record.setVaccinationDate(request.vaccinationDate());
        record.setNotes(normalize(request.notes()));

        // Business rule: nextDueDate is ALWAYS calculated by the service.
        record.setNextDueDate(calculateNextDueDate(
                request.vaccinationDate(), vaccineType.getIntervalDays()));

        return toResponse(vaccinationRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> history(Long petId) {
        petService.getPet(petId);
        return vaccinationRepository.findByPetPetIdOrderByVaccinationDateDesc(petId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DueVaccinationResponse> dueWithin(int days) {
        if (days < 0 || days > 365) {
            throw new BusinessRuleException("days must be between 0 and 365.");
        }

        LocalDate today = LocalDate.now();
        LocalDate end = today.plusDays(days);

        var user = currentUserService == null ? null : currentUserService.getCurrentUserOrNull();
        var records = user == null
                ? vaccinationRepository.findAll().stream()
                    .filter(r -> !r.getNextDueDate().isBefore(today) && !r.getNextDueDate().isAfter(end))
                    .toList()
                : vaccinationRepository.findByPetOwnerUserUserIdAndNextDueDateBetweenOrderByNextDueDateAsc(
                    user.getUserId(), today, end);

        return records.stream()
                .map(this::toDueResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VaccinationResponse findById(Long id) {
        return toResponse(getRecord(id));
    }

    @Transactional
    public VaccinationResponse update(Long id, VaccinationRequest request) {
        VaccinationRecord record = getRecord(id);
        Pet pet = record.getPet();
        VaccineType vaccineType = vaccineTypeService.getType(request.vaccineTypeId());

        validateVaccinationDate(pet, request.vaccinationDate());

        record.setVaccinationDate(request.vaccinationDate());
        record.setVaccineType(vaccineType);
        record.setNotes(normalize(request.notes()));

        // Recalculate on every update; never trust a client-supplied due date.
        record.setNextDueDate(calculateNextDueDate(
                request.vaccinationDate(), vaccineType.getIntervalDays()));

        return toResponse(vaccinationRepository.save(record));
    }

    @Transactional
    public void delete(Long id) {
        vaccinationRepository.delete(getRecord(id));
    }

    public VaccinationRecord getRecord(Long id) {
        var user = currentUserService == null ? null : currentUserService.getCurrentUserOrNull();
        if (user == null) {
            return vaccinationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Vaccination record not found with ID: " + id));
        }
        return vaccinationRepository.findByVaccinationIdAndPetOwnerUserUserId(id, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vaccination record not found in your account with ID: " + id));
    }

    private void validateVaccinationDate(Pet pet, LocalDate vaccinationDate) {
        if (vaccinationDate.isAfter(LocalDate.now())) {
            throw new BusinessRuleException("Vaccination date cannot be in the future.");
        }

        if (vaccinationDate.isBefore(pet.getDateOfBirth())) {
            throw new BusinessRuleException(
                    "Vaccination date cannot be before the pet's date of birth.");
        }
    }

    private LocalDate calculateNextDueDate(LocalDate vaccinationDate, int intervalDays) {
        if (intervalDays <= 0) {
            throw new BusinessRuleException(
                    "Vaccine type must have a positive interval before a vaccination can be saved.");
        }
        return vaccinationDate.plusDays(intervalDays);
    }

    private VaccinationResponse toResponse(VaccinationRecord record) {
        return new VaccinationResponse(
                record.getVaccinationId(),
                record.getPet().getPetId(),
                record.getPet().getPetName(),
                record.getVaccineType().getVaccineTypeId(),
                record.getVaccineType().getVaccineName(),
                record.getVaccinationDate(),
                record.getNextDueDate(),
                record.getNotes()
        );
    }

    private DueVaccinationResponse toDueResponse(VaccinationRecord record) {
        return new DueVaccinationResponse(
                record.getVaccinationId(),
                record.getPet().getPetId(),
                record.getPet().getPetName(),
                record.getPet().getOwner().getOwnerName(),
                record.getVaccineType().getVaccineName(),
                record.getVaccinationDate(),
                record.getNextDueDate()
        );
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
