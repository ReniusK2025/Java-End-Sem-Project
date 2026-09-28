package com.example.PetCareBook.Controller;

import com.example.PetCareBook.DTO.OwnerRequest;
import com.example.PetCareBook.DTO.PetRequest;
import com.example.PetCareBook.DTO.VaccinationRequest;
import com.example.PetCareBook.DTO.VaccineTypeRequest;
import com.example.PetCareBook.Service.OwnerService;
import com.example.PetCareBook.Service.PetService;
import com.example.PetCareBook.Service.VaccinationService;
import com.example.PetCareBook.Service.VaccineTypeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui")
public class UiActionController {

    private final OwnerService ownerService;
    private final PetService petService;
    private final VaccineTypeService vaccineTypeService;
    private final VaccinationService vaccinationService;

    public UiActionController(
            OwnerService ownerService,
            PetService petService,
            VaccineTypeService vaccineTypeService,
            VaccinationService vaccinationService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.vaccineTypeService = vaccineTypeService;
        this.vaccinationService = vaccinationService;
    }

    @PostMapping("/owners")
    public String addOwner(@Valid OwnerRequest request, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/owners";
        }
        try {
            ownerService.create(request);
            ra.addFlashAttribute("successMessage", "Owner added successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/owners";
    }

    @PostMapping("/owners/{id}/update")
    public String updateOwner(@PathVariable Long id, @Valid OwnerRequest request,
                              BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/owners?edit=" + id;
        }
        try {
            ownerService.update(id, request);
            ra.addFlashAttribute("successMessage", "Owner updated successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/owners";
    }

    @PostMapping("/owners/{id}/delete")
    public String deleteOwner(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ownerService.delete(id);
            ra.addFlashAttribute("successMessage", "Owner deleted successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/owners";
    }

    @PostMapping("/pets")
    public String addPet(@Valid PetRequest request, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/pets";
        }
        try {
            petService.create(request);
            ra.addFlashAttribute("successMessage", "Pet registered successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/pets";
    }

    @PostMapping("/pets/{id}/update")
    public String updatePet(@PathVariable Long id, @Valid PetRequest request,
                            BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/pets?edit=" + id;
        }
        try {
            petService.update(id, request);
            ra.addFlashAttribute("successMessage", "Pet updated successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/pets";
    }

    @PostMapping("/pets/{id}/delete")
    public String deletePet(@PathVariable Long id, RedirectAttributes ra) {
        try {
            petService.delete(id);
            ra.addFlashAttribute("successMessage", "Pet deleted successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/pets";
    }

    @PostMapping("/vaccines")
    public String addVaccine(@Valid VaccineTypeRequest request, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/vaccines";
        }
        try {
            vaccineTypeService.create(request);
            ra.addFlashAttribute("successMessage",
                    "Vaccine type '" + request.vaccineName().trim() + "' added successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/vaccines";
    }

    @PostMapping("/vaccines/{id}/update")
    public String updateVaccine(@PathVariable Long id, @Valid VaccineTypeRequest request,
                                BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/vaccines?edit=" + id;
        }
        try {
            vaccineTypeService.update(id, request);
            ra.addFlashAttribute("successMessage", "Vaccine type updated successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/vaccines";
    }

    @PostMapping("/vaccines/{id}/delete")
    public String deleteVaccine(@PathVariable Long id, RedirectAttributes ra) {
        try {
            vaccineTypeService.delete(id);
            ra.addFlashAttribute("successMessage", "Vaccine type deleted successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/vaccines";
    }

    @PostMapping("/pets/{petId}/vaccinations")
    public String addVaccination(@PathVariable Long petId, @Valid VaccinationRequest request,
                                 BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/pets";
        }
        try {
            vaccinationService.record(petId, request);
            ra.addFlashAttribute("successMessage", "Vaccination recorded successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/pets";
    }

    @PostMapping("/vaccinations/{id}/update")
    public String updateVaccination(@PathVariable Long id, @Valid VaccinationRequest request,
                                    BindingResult result, RedirectAttributes ra) {
        var current = vaccinationService.findById(id);
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", firstError(result));
            return "redirect:/pets/" + current.petId() + "?edit=" + id;
        }
        try {
            vaccinationService.update(id, request);
            ra.addFlashAttribute("successMessage", "Vaccination record updated successfully.");
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/pets/" + current.petId();
    }

    @PostMapping("/vaccinations/{id}/delete")
    public String deleteVaccination(@PathVariable Long id, RedirectAttributes ra) {
        try {
            var current = vaccinationService.findById(id);
            vaccinationService.delete(id);
            ra.addFlashAttribute("successMessage", "Vaccination record deleted successfully.");
            return "redirect:/pets/" + current.petId();
        } catch (RuntimeException ex) {
            ra.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/pets";
        }
    }

    private String firstError(BindingResult result) {
        return result.getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Please check the entered details.");
    }
}
