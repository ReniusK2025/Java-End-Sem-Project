package com.example.PetCareBook.Service;

import com.example.PetCareBook.DTO.PetRequest;
import com.example.PetCareBook.DTO.PetResponse;
import com.example.PetCareBook.Exception.ResourceNotFoundException;
import com.example.PetCareBook.Model.Owner;
import com.example.PetCareBook.Model.Pet;
import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerService ownerService;
    private final CurrentUserService currentUserService;

    public PetService(PetRepository petRepository, OwnerService ownerService,
                      CurrentUserService currentUserService) {
        this.petRepository = petRepository;
        this.ownerService = ownerService;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public PetResponse create(PetRequest request) {
        Owner owner = ownerService.getOwner(request.ownerId());
        Pet pet = new Pet();
        apply(pet, request, owner);
        return toResponse(petRepository.save(pet));
    }

    @Transactional(readOnly = true)
    public List<PetResponse> findAll(String name) {
        UserAccount user = currentUserService.getCurrentUserOrNull();
        List<Pet> pets;

        if (user == null) {
            pets = (name == null || name.isBlank())
                    ? petRepository.findAll()
                    : petRepository.findByPetNameContainingIgnoreCase(name.trim());
        } else {
            pets = (name == null || name.isBlank())
                    ? petRepository.findByOwnerUserUserIdOrderByPetIdAsc(user.getUserId())
                    : petRepository.findByOwnerUserUserIdAndPetNameContainingIgnoreCaseOrderByPetIdAsc(
                            user.getUserId(), name.trim());
        }

        return pets.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PetResponse findById(Long id) {
        return toResponse(getPet(id));
    }

    @Transactional
    public PetResponse update(Long id, PetRequest request) {
        Pet pet = getPet(id);
        Owner owner = ownerService.getOwner(request.ownerId());
        apply(pet, request, owner);
        return toResponse(petRepository.save(pet));
    }

    @Transactional
    public void delete(Long id) {
        petRepository.delete(getPet(id));
    }

    public Pet getPet(Long id) {
        UserAccount user = currentUserService.getCurrentUserOrNull();

        if (user == null) {
            return petRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + id));
        }

        return petRepository.findByPetIdAndOwnerUserUserId(id, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found in your account with ID: " + id));
    }

    private void apply(Pet pet, PetRequest request, Owner owner) {
        pet.setPetName(request.petName().trim());
        pet.setSpecies(request.species().trim());
        pet.setBreed(request.breed().trim());
        pet.setDateOfBirth(request.dateOfBirth());
        pet.setOwner(owner);
    }

    private PetResponse toResponse(Pet pet) {
        return new PetResponse(pet.getPetId(), pet.getPetName(), pet.getSpecies(),
                pet.getBreed(), pet.getDateOfBirth(),
                pet.getOwner().getOwnerId(), pet.getOwner().getOwnerName());
    }
}
