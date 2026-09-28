package com.example.PetCareBook.Controller;

import com.example.PetCareBook.DTO.VaccineTypeRequest;
import com.example.PetCareBook.DTO.VaccineTypeResponse;
import com.example.PetCareBook.Service.VaccineTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaccine-types")
public class VaccineTypeController {

    private final VaccineTypeService vaccineTypeService;

    public VaccineTypeController(VaccineTypeService vaccineTypeService) {
        this.vaccineTypeService = vaccineTypeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VaccineTypeResponse create(@Valid @RequestBody VaccineTypeRequest request) {
        return vaccineTypeService.create(request);
    }

    @GetMapping
    public List<VaccineTypeResponse> findAll() {
        return vaccineTypeService.findAll();
    }

    @GetMapping("/{id}")
    public VaccineTypeResponse findById(@PathVariable Long id) {
        return vaccineTypeService.findById(id);
    }

    @PutMapping("/{id}")
    public VaccineTypeResponse update(
            @PathVariable Long id,
            @Valid @RequestBody VaccineTypeRequest request) {
        return vaccineTypeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vaccineTypeService.delete(id);
    }
}
