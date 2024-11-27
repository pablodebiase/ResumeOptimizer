package org.resumeoptimizer.controllers;

import org.resumeoptimizer.services.CommandExecutorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProcessController {

    private final CommandExecutorService commandExecutorService;

    public ProcessController(CommandExecutorService commandExecutorService) {
        this.commandExecutorService = commandExecutorService;
    }

    @GetMapping("/process/{id}")
    public String process(@PathVariable Long id, Model model) {
        // Start the command execution asynchronously
        commandExecutorService.executeCommands(id);

        // Return the log page immediately
        model.addAttribute("log", "Initializing logs...");
        return "log"; // JSP page for logs
    }
}