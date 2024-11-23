package org.resumeoptimizer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.resumeoptimizer.entities.User;
import org.resumeoptimizer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService service) {
        this.userService = service;
    }

    // Registration page
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Handle registration
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "User already exists");
            return "register";
        }
        user.setRole("USER");
        userService.save(user);
        return "redirect:/login";
    }

    // Login
    @GetMapping("/login")
    public String showLoginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }

    // Handle login POST (if needed)
    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpServletRequest request) {
        User existingUser = userService.findByUsername(user.getUsername());
        if (existingUser == null) {
            model.addAttribute("error", "User does not exist");
        } else if (!userService.matchesPassword(user.getPassword(), existingUser.getPassword())) {
            model.addAttribute("error", "Password is incorrect");
        } else {
            request.getSession().setAttribute("user", existingUser);
            return "redirect:/upload";
        }
        return "login";
    }

    // Guest login
    @GetMapping("/guest")
    public String guestLogin(HttpSession session) {
        // Create a guest user session
        User guestUser = new User();
        guestUser.setUsername("guest_" + UUID.randomUUID());
        guestUser.setRole("GUEST");
        session.setAttribute("guestUser", guestUser);
        return "redirect:/upload";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }
}
