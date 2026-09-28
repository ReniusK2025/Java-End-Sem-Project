package com.example.PetCareBook.Service;

import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserAccountRepository repository;
    private final DefaultVaccineTypeService defaultVaccineTypeService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserAccountRepository repository, DefaultVaccineTypeService defaultVaccineTypeService) {
        this.repository = repository;
        this.defaultVaccineTypeService = defaultVaccineTypeService;
    }

    public UserAccount register(String username, String email, String fullName, String password) {
        username = username.trim();
        email = email.trim().toLowerCase();
        fullName = fullName.trim();

        if (repository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username already exists. Please choose another username.");
        }
        if (repository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        UserAccount user = new UserAccount();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setActive(true);

        UserAccount savedUser = repository.save(user);
        defaultVaccineTypeService.seedForUser(savedUser);
        return savedUser;
    }

    public UserAccount authenticate(String username, String password) {
        return repository.findByUsernameIgnoreCaseAndActiveTrue(username.trim())
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .orElse(null);
    }
}
