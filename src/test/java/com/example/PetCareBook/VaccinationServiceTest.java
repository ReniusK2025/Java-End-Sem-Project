package com.example.PetCareBook;

import com.example.PetCareBook.DTO.VaccinationRequest;
import com.example.PetCareBook.DTO.VaccinationResponse;
import com.example.PetCareBook.Model.Pet;
import com.example.PetCareBook.Model.VaccineType;
import com.example.PetCareBook.Model.VaccinationRecord;
import com.example.PetCareBook.Repository.VaccinationRecordRepository;
import com.example.PetCareBook.Service.PetService;
import com.example.PetCareBook.Service.VaccinationService;
import com.example.PetCareBook.Service.VaccineTypeService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VaccinationServiceTest {

    @Test
    void shouldCalculateNextDueDateFromVaccineInterval() {
        VaccinationRecordRepository repository = mock(VaccinationRecordRepository.class);
        PetService petService = mock(PetService.class);
        VaccineTypeService vaccineTypeService = mock(VaccineTypeService.class);

        VaccinationService service =
                new VaccinationService(repository, petService, vaccineTypeService);

        Pet pet = new Pet();
        pet.setPetId(1L);
        pet.setPetName("Bruno");
        pet.setDateOfBirth(LocalDate.of(2022, 1, 1));

        VaccineType vaccine = new VaccineType();
        vaccine.setVaccineTypeId(1L);
        vaccine.setVaccineName("Demo Rabies");
        vaccine.setIntervalDays(365);

        when(petService.getPet(1L)).thenReturn(pet);
        when(vaccineTypeService.getType(1L)).thenReturn(vaccine);

        when(repository.save(any(VaccinationRecord.class)))
                .thenAnswer(invocation -> {
                    VaccinationRecord saved = invocation.getArgument(0);
                    saved.setVaccinationId(10L);
                    return saved;
                });

        VaccinationResponse response = service.record(
                1L,
                new VaccinationRequest(
                        LocalDate.of(2026, 9, 28),
                        1L,
                        "Annual"
                )
        );

        assertEquals(LocalDate.of(2027, 9, 28), response.nextDueDate());
        assertEquals("Demo Rabies", response.vaccineName());
        verify(repository).save(any(VaccinationRecord.class));
    }
}
