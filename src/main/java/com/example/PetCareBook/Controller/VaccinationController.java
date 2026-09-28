package com.example.PetCareBook.Controller;

import com.example.PetCareBook.DTO.DueVaccinationResponse;
import com.example.PetCareBook.DTO.VaccinationRequest;
import com.example.PetCareBook.DTO.VaccinationResponse;
import com.example.PetCareBook.Service.VaccinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccinations")
public class VaccinationController {

    private final VaccinationService vaccinationService;

    public VaccinationController(VaccinationService vaccinationService) {
        this.vaccinationService = vaccinationService;
    }

    @GetMapping("/due")
    public List<DueVaccinationResponse> due(
            @RequestParam(defaultValue = "7") int days) {
        return vaccinationService.dueWithin(days);
    }

    @GetMapping("/{id}")
    public VaccinationResponse findById(@PathVariable Long id) {
        return vaccinationService.findById(id);
    }

    @PutMapping("/{id}")
    public VaccinationResponse update(
            @PathVariable Long id,
            @Valid @RequestBody VaccinationRequest request) {
        return vaccinationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vaccinationService.delete(id);
    }
}
