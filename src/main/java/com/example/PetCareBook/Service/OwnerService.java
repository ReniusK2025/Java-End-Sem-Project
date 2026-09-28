package com.example.PetCareBook.Service;

import com.example.PetCareBook.DTO.OwnerRequest;
import com.example.PetCareBook.DTO.OwnerResponse;
import com.example.PetCareBook.Exception.BusinessRuleException;
import com.example.PetCareBook.Exception.DuplicateResourceException;
import com.example.PetCareBook.Exception.ResourceNotFoundException;
import com.example.PetCareBook.Model.Owner;
import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Repository.OwnerRepository;
import com.example.PetCareBook.Repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final CurrentUserService currentUserService;

    public OwnerService(OwnerRepository ownerRepository, PetRepository petRepository,
                        CurrentUserService currentUserService) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public OwnerResponse create(OwnerRequest request) {
        UserAccount user = currentUserService.getCurrentUserOrNull();
        String email = request.email().trim().toLowerCase();

        if (user != null) {
            if (ownerRepository.existsByUserUserIdAndEmailIgnoreCase(user.getUserId(), email)) {
                throw new DuplicateResourceException("An owner with this email already exists in your account.");
            }
        } else if (ownerRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("An owner with email '" + email + "' already exists.");
        }

        Owner owner = new Owner();
        owner.setOwnerName(request.ownerName().trim());
        owner.setEmail(email);
        owner.setPhone(normalize(request.phone()));
        owner.setUser(user);

        return toResponse(ownerRepository.save(owner));
    }

    @Transactional(readOnly = true)
    public List<OwnerResponse> findAll() {
        UserAccount user = currentUserService.getCurrentUserOrNull();
        List<Owner> owners = user == null
                ? ownerRepository.findAll()
                : ownerRepository.findByUserUserIdOrderByOwnerIdAsc(user.getUserId());
        return owners.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OwnerResponse findById(Long id) {
        return toResponse(getOwner(id));
    }

    @Transactional
    public OwnerResponse update(Long id, OwnerRequest request) {
        Owner owner = getOwner(id);
        UserAccount user = currentUserService.getCurrentUserOrNull();
        String email = request.email().trim().toLowerCase();

        if (user != null) {
            ownerRepository.findByUserUserIdAndEmailIgnoreCase(user.getUserId(), email)
                    .filter(existing -> !existing.getOwnerId().equals(id))
                    .ifPresent(existing -> {
                        throw new DuplicateResourceException(
                                "An owner with this email already exists in your account.");
                    });
        }

        owner.setOwnerName(request.ownerName().trim());
        owner.setEmail(email);
        owner.setPhone(normalize(request.phone()));
        return toResponse(ownerRepository.save(owner));
    }

    @Transactional
    public void delete(Long id) {
        getOwner(id);

        if (!petRepository.findByOwnerOwnerId(id).isEmpty()) {
            throw new BusinessRuleException(
                    "Cannot delete an owner who still has pets. Delete or reassign the pets first.");
        }

        ownerRepository.deleteById(id);
    }

    public Owner getOwner(Long id) {
        UserAccount user = currentUserService.getCurrentUserOrNull();

        if (user == null) {
            return ownerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Owner not found with ID: " + id));
        }

        return ownerRepository.findByOwnerIdAndUserUserId(id, user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found in your account with ID: " + id));
    }

    private OwnerResponse toResponse(Owner owner) {
        return new OwnerResponse(owner.getOwnerId(), owner.getOwnerName(),
                owner.getEmail(), owner.getPhone());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
