package org.resumeoptimizer.controllers;

import org.resumeoptimizer.entities.UploadSession;
import org.resumeoptimizer.repositories.UploadSessionRepository;
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

    private final UploadSessionRepository uploadSessionRepository;
    private final UserService userService;

    @Autowired
    public ScoresController(UploadSessionRepository uploadSessionRepository, UserService userService) {
        this.uploadSessionRepository = uploadSessionRepository;
        this.userService = userService;
    }

    @GetMapping("/scores")
    public String scores(Authentication authentication, Model model) {
        String username = userService.findByUsername(authentication.getName()).getUsername(); // Get logged-in username
        List<UploadSession> sessions = uploadSessionRepository.findByUsername(username);

        // Add scores to the model
        model.addAttribute("scores", sessions.stream()
                .map(session -> Map.of("date", session.date(), "resume", session.getResumeFileName(), "jobDesc", session.getJobDescFileName(), "value", String.format("%.2f", session.getScore() * 100)))
                .collect(Collectors.toList()));
        return "scores"; // Maps to WEB-INF/views/scores.jsp
    }
}
