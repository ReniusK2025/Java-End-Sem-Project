package com.example.PetCareBook.Service;

import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Model.VaccineType;
import com.example.PetCareBook.Repository.VaccineTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultVaccineTypeService {

    private final VaccineTypeRepository vaccineTypeRepository;

    public DefaultVaccineTypeService(VaccineTypeRepository vaccineTypeRepository) {
        this.vaccineTypeRepository = vaccineTypeRepository;
    }

    /**
     * Gives each account its own copy of the standard PetCareBook vaccine/care types.
     * Existing entries with the same name are reused instead of duplicated.
     */
    public void seedForUser(UserAccount user) {
        seed(vaccineTypeRepository, user, "Rabies",
                "Rabies is a serious viral disease that affects the nervous system of animals and humans. It is mainly spread through the bite or saliva of an infected animal. Rabies vaccination helps protect pets from this deadly disease and is an important part of regular pet healthcare.", 365);
        seed(vaccineTypeRepository, user, "DHPP",
                "Combination canine vaccine covering distemper, adenovirus, parvovirus and parainfluenza.", 365);
        seed(vaccineTypeRepository, user, "FVRCP",
                "Combination feline vaccine covering feline viral rhinotracheitis, calicivirus and panleukopenia.", 365);
        seed(vaccineTypeRepository, user, "Bordetella",
                "Helps protect dogs against Bordetella bronchiseptica, commonly associated with kennel cough.", 180);
        seed(vaccineTypeRepository, user, "Deworming",
                "Parasite-control treatment used in a pet wellness schedule; this entry is not a vaccine.", 90);
    }

    private void seed(
            VaccineTypeRepository repository,
            UserAccount user,
            String name,
            String description,
            int intervalDays) {

        var existing = repository.findByUserUserIdAndVaccineNameIgnoreCase(user.getUserId(), name);

        if (existing.isEmpty()) {
            VaccineType type = new VaccineType();
            type.setVaccineName(name);
            type.setDescription(description);
            type.setIntervalDays(intervalDays);
            type.setUser(user);
            repository.save(type);
            return;
        }

        VaccineType type = existing.get();
        if (type.getDescription() == null
                || type.getDescription().isBlank()
                || type.getDescription().equals("Demo interval for the academic project.")) {
            type.setDescription(description);
            type.setIntervalDays(intervalDays);
            repository.save(type);
        }
    }
}
