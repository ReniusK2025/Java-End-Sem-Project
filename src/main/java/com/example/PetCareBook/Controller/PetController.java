package com.example.PetCareBook.Controller;

import com.example.PetCareBook.DTO.PetRequest;
import com.example.PetCareBook.DTO.PetResponse;
import com.example.PetCareBook.DTO.VaccinationResponse;
import com.example.PetCareBook.Service.PetService;
import com.example.PetCareBook.Service.VaccinationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private final VaccinationService vaccinationService;

    public PetController(PetService petService, VaccinationService vaccinationService) {
        this.petService = petService;
        this.vaccinationService = vaccinationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetResponse create(@Valid @RequestBody PetRequest request) {
        return petService.create(request);
    }

    @GetMapping
    public List<PetResponse> findAll(
            @RequestParam(required = false) String name) {
        return petService.findAll(name);
    }

    @GetMapping("/{id}")
    public PetResponse findById(@PathVariable Long id) {
        return petService.findById(id);
    }

    @PutMapping("/{id}")
    public PetResponse update(
            @PathVariable Long id,
            @Valid @RequestBody PetRequest request) {
        return petService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        petService.delete(id);
    }

    @GetMapping("/{petId}/vaccination-history")
    public List<VaccinationResponse> vaccinationHistory(@PathVariable Long petId) {
        return vaccinationService.history(petId);
    }

    @PostMapping("/{petId}/vaccinations")
    @ResponseStatus(HttpStatus.CREATED)
    public VaccinationResponse recordVaccination(
            @PathVariable Long petId,
            @Valid @RequestBody com.example.PetCareBook.DTO.VaccinationRequest request) {
        return vaccinationService.record(petId, request);
    }
}
