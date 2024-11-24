package org.resumeoptimizer.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.resumeoptimizer.entities.User;
import org.resumeoptimizer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
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
            String errorName = "User does not exist";
            System.out.println("Error: " + errorName);
            model.addAttribute("error", errorName);
        } else if (!userService.matchesPassword(user.getPassword(), existingUser.getPassword())) {
            String errorName = "Password is incorrect";
            System.out.println("Error: " + errorName);
            model.addAttribute("error", errorName);
        } else if (Objects.equals(existingUser.getRole(), "GUEST")){
            String errorName = "Cannot login as guest";
            System.out.println("Error: " + errorName);
            model.addAttribute("error", errorName);
        }
        else {
            request.getSession().setAttribute("user", existingUser);
            return "redirect:/upload";
        }
        return "login";
    }

    @GetMapping("/guest")
    public String guestLogin(HttpServletRequest request) {
        // Create a guest user
        User guestUser = new User();
        guestUser.setUsername("guest_" + UUID.randomUUID());
        guestUser.setPassword(UUID.randomUUID().toString());
        guestUser.setRole("GUEST");
        System.out.println("Guest user: " + guestUser.getUsername() + " " + guestUser.getPassword());
        userService.save(guestUser);

        // Set authentication
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_GUEST"));
        Authentication auth = new UsernamePasswordAuthenticationToken(guestUser, null, authorities);

        // Persist authentication in SecurityContextHolder and session
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

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
