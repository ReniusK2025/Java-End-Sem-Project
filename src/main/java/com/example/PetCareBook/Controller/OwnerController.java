package com.example.PetCareBook.Controller;

import com.example.PetCareBook.DTO.OwnerRequest;
import com.example.PetCareBook.DTO.OwnerResponse;
import com.example.PetCareBook.Service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponse create(@Valid @RequestBody OwnerRequest request) {
        return ownerService.create(request);
    }

    @GetMapping
    public List<OwnerResponse> findAll() {
        return ownerService.findAll();
    }

    @GetMapping("/{id}")
    public OwnerResponse findById(@PathVariable Long id) {
        return ownerService.findById(id);
    }

    @PutMapping("/{id}")
    public OwnerResponse update(
            @PathVariable Long id,
            @Valid @RequestBody OwnerRequest request) {
        return ownerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ownerService.delete(id);
    }
}
