package com.example.PetCareBook.Controller;

import com.example.PetCareBook.Service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {

        var user = authService.authenticate(username, password);

        if (user != null) {
            session.setAttribute("loggedInUser", user.getUsername());
            session.setAttribute("loggedInUserId", user.getUserId());
            session.setAttribute("loggedInFullName", user.getFullName());
            return "redirect:/";
        }

        return "redirect:/login?error=true";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword) {

        if (fullName.trim().length() < 2
                || username.trim().length() < 3
                || !email.contains("@")
                || password.length() < 6) {
            return "redirect:/register?error=validation";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=mismatch";
        }

        try {
            authService.register(username, email, fullName, password);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException ex) {
            return "redirect:/register?error=exists";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
