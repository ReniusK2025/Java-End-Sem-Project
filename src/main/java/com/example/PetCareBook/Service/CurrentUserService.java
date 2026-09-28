package com.example.PetCareBook.Service;

import com.example.PetCareBook.Model.UserAccount;
import com.example.PetCareBook.Repository.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class CurrentUserService {

    private final UserAccountRepository repository;

    public CurrentUserService(UserAccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the logged-in account for a web/API request.
     * Returns null when called outside an HTTP request (for example, a unit test).
     */
    public UserAccount getCurrentUserOrNull() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) {
            return null;
        }

        HttpServletRequest request = attrs.getRequest();
        Object userId = request.getSession(false) == null
                ? null
                : request.getSession(false).getAttribute("loggedInUserId");

        if (!(userId instanceof Long id)) {
            return null;
        }

        return repository.findById(id).orElse(null);
    }
}
