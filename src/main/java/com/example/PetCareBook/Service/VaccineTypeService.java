package com.example.PetCareBook.Service;

import com.example.PetCareBook.DTO.VaccineTypeRequest;
import com.example.PetCareBook.DTO.VaccineTypeResponse;
import com.example.PetCareBook.Exception.BusinessRuleException;
import com.example.PetCareBook.Exception.DuplicateResourceException;
import com.example.PetCareBook.Exception.ResourceNotFoundException;
import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Model.VaccineType;
import com.example.PetCareBook.Repository.VaccineTypeRepository;
import com.example.PetCareBook.Repository.VaccinationRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VaccineTypeService {

    private final VaccineTypeRepository vaccineTypeRepository;
    private final VaccinationRecordRepository vaccinationRecordRepository;
    private final CurrentUserService currentUserService;

    public VaccineTypeService(VaccineTypeRepository vaccineTypeRepository,
                              VaccinationRecordRepository vaccinationRecordRepository,
                              CurrentUserService currentUserService) {
        this.vaccineTypeRepository = vaccineTypeRepository;
        this.vaccinationRecordRepository = vaccinationRecordRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public VaccineTypeResponse create(VaccineTypeRequest request) {
        UserAccount user = currentUserService.getCurrentUserOrNull();
        String name = request.vaccineName().trim();

        if (user != null && vaccineTypeRepository.existsByUserUserIdAndVaccineNameIgnoreCase(user.getUserId(), name)) {
            throw new DuplicateResourceException("Vaccine type '" + name + "' already exists in your account.");
        }
        if (user == null && vaccineTypeRepository.existsByVaccineNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Vaccine type '" + name + "' already exists.");
        }

        VaccineType type = new VaccineType();
        apply(type, request);
        type.setUser(user);
        return toResponse(vaccineTypeRepository.save(type));
    }

    @Transactional(readOnly = true)
    public List<VaccineTypeResponse> findAll() {
        UserAccount user = currentUserService.getCurrentUserOrNull();
        List<VaccineType> types = user == null
                ? vaccineTypeRepository.findAll()
                : vaccineTypeRepository.findByUserUserIdOrderByVaccineTypeIdAsc(user.getUserId());
        return types.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VaccineTypeResponse findById(Long id) {
        return toResponse(getType(id));
    }

    @Transactional
    public VaccineTypeResponse update(Long id, VaccineTypeRequest request) {
        VaccineType type = getType(id);
        UserAccount user = currentUserService.getCurrentUserOrNull();
        String name = request.vaccineName().trim();

        if (user != null) {
            vaccineTypeRepository.findByUserUserIdAndVaccineNameIgnoreCase(user.getUserId(), name)
                    .filter(existing -> !existing.getVaccineTypeId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException(
                                "Vaccine type '" + name + "' already exists in your account.");
                    });
        }

        apply(type, request);
        return toResponse(vaccineTypeRepository.save(type));
    }

    @Transactional
    public void delete(Long id) {
        VaccineType type = getType(id);
        if (vaccinationRecordRepository.countByVaccineTypeVaccineTypeId(id) > 0) {
            throw new BusinessRuleException("Cannot delete vaccine type '" + type.getVaccineName()
                    + "' because vaccination records already use it.");
        }
        vaccineTypeRepository.delete(type);
    }

    public VaccineType getType(Long id) {
        UserAccount user = currentUserService.getCurrentUserOrNull();

        if (user == null) {
            return vaccineTypeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Vaccine type not found with ID: " + id));
        }

        return vaccineTypeRepository.findByVaccineTypeIdAndUserUserId(id, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vaccine type not found in your account with ID: " + id));
    }

    private void apply(VaccineType type, VaccineTypeRequest request) {
        type.setVaccineName(request.vaccineName().trim());
        type.setDescription(request.description() == null ? null : request.description().trim());
        type.setIntervalDays(request.intervalDays());
    }

    private VaccineTypeResponse toResponse(VaccineType type) {
        return new VaccineTypeResponse(type.getVaccineTypeId(), type.getVaccineName(),
                type.getDescription(), type.getIntervalDays());
    }
}
