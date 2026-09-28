package com.example.PetCareBook.Controller;

import com.example.PetCareBook.Service.OwnerService;
import com.example.PetCareBook.Service.PetService;
import com.example.PetCareBook.Service.VaccinationService;
import com.example.PetCareBook.Service.VaccineTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    private final OwnerService ownerService;
    private final PetService petService;
    private final VaccineTypeService vaccineTypeService;
    private final VaccinationService vaccinationService;

    public WebController(
            OwnerService ownerService,
            PetService petService,
            VaccineTypeService vaccineTypeService,
            VaccinationService vaccinationService) {
        this.ownerService = ownerService;
        this.petService = petService;
        this.vaccineTypeService = vaccineTypeService;
        this.vaccinationService = vaccinationService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        var owners = ownerService.findAll();
        var pets = petService.findAll(null);
        var due = vaccinationService.dueWithin(7);

        model.addAttribute("ownerCount", owners.size());
        model.addAttribute("petCount", pets.size());
        model.addAttribute("dueCount", due.size());
        model.addAttribute("dueVaccinations", due);
        return "dashboard";
    }

    @GetMapping("/pets")
    public String pets(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("pets", petService.findAll(null));
        model.addAttribute("owners", ownerService.findAll());
        model.addAttribute("vaccines", vaccineTypeService.findAll());
        if (edit != null) {
            model.addAttribute("editPet", petService.findById(edit));
        }
        return "pets";
    }

    @GetMapping("/pets/{id}")
    public String petHistory(
            @PathVariable Long id,
            @RequestParam(required = false) Long edit,
            Model model) {
        model.addAttribute("pet", petService.findById(id));
        model.addAttribute("history", vaccinationService.history(id));
        model.addAttribute("vaccines", vaccineTypeService.findAll());
        if (edit != null) {
            var record = vaccinationService.findById(edit);
            if (!record.petId().equals(id)) {
                throw new IllegalArgumentException("Vaccination record does not belong to this pet.");
            }
            model.addAttribute("editVaccination", record);
        }
        return "pet-history";
    }

    @GetMapping("/owners")
    public String owners(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("owners", ownerService.findAll());
        if (edit != null) {
            model.addAttribute("editOwner", ownerService.findById(edit));
        }
        return "owners";
    }

    @GetMapping("/vaccines")
    public String vaccines(@RequestParam(required = false) Long edit, Model model) {
        model.addAttribute("vaccines", vaccineTypeService.findAll());
        if (edit != null) {
            model.addAttribute("editVaccine", vaccineTypeService.findById(edit));
        }
        return "vaccines";
    }
}
