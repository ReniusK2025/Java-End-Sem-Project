package com.example.PetCareBook.Config;

import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Repository.OwnerRepository;
import com.example.PetCareBook.Repository.UserAccountRepository;
import com.example.PetCareBook.Service.DefaultVaccineTypeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    @Order(1)
    CommandLineRunner seedDemoUser(UserAccountRepository repository) {
        return args -> {
            if (!repository.existsByUsernameIgnoreCase("admin")) {
                UserAccount user = new UserAccount();
                user.setUsername("admin");
                user.setEmail("admin@petcarebook.local");
                user.setFullName("System Administrator");
                user.setPasswordHash(new BCryptPasswordEncoder().encode("admin123"));
                user.setActive(true);
                repository.save(user);
            }
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner seedAndMigrateDemoData(
            UserAccountRepository userRepository,
            OwnerRepository ownerRepository,
            DefaultVaccineTypeService defaultVaccineTypeService,
            JdbcTemplate jdbcTemplate) {

        return args -> {
            // Existing databases may still have description as VARCHAR(255) from an older version.
            // Explicitly migrate it because Hibernate ddl-auto=update does not reliably widen this
            // existing column when the entity changes to TEXT.
            jdbcTemplate.execute("ALTER TABLE vaccine_types MODIFY COLUMN description TEXT");

            UserAccount admin = userRepository.findByUsernameIgnoreCaseAndActiveTrue("admin")
                    .orElseThrow();

            // Existing records from earlier versions belong to the demo/admin account.
            ownerRepository.findAll().stream()
                    .filter(owner -> owner.getUser() == null)
                    .forEach(owner -> {
                        owner.setUser(admin);
                        ownerRepository.save(owner);
                    });

            // Keep the admin account's standard library and give every existing account
            // its own copy of the standard vaccine/care types.
            userRepository.findAll().stream()
                    .filter(UserAccount::isActive)
                    .forEach(defaultVaccineTypeService::seedForUser);
        };
    }
}
