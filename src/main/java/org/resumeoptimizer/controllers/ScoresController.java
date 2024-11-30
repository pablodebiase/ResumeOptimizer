package org.resumeoptimizer.controllers;

import org.resumeoptimizer.entities.Session;
import org.resumeoptimizer.repositories.SessionRepository;
import org.resumeoptimizer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ScoresController {

    private final SessionRepository sessionRepository;
    private final UserService userService;

    @Autowired
    public ScoresController(SessionRepository sessionRepository, UserService userService) {
        this.sessionRepository = sessionRepository;
        this.userService = userService;
    }

    @GetMapping("/scores")
    public String scores(Authentication authentication, Model model) {
        String username = userService.findByUsername(authentication.getName()).getUsername(); // Get logged-in username
        List<Session> sessions = sessionRepository.findByUsername(username);

        // Add scores to the model
        model.addAttribute("scores", sessions.stream()
                .map(session -> Map.of("date", session.date(), "resume", session.getResumeFileName(), "jobDesc", session.getJobDescFileName(), "value", String.format("%.2f", session.getScore() * 100)))
                .collect(Collectors.toList()));
        return "scores"; // Maps to WEB-INF/views/scores.jsp
    }
}
