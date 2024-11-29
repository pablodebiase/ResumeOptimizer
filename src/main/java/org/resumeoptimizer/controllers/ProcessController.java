package org.resumeoptimizer.controllers;

import org.resumeoptimizer.repositories.UploadSessionRepository;
import org.resumeoptimizer.services.CommandExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProcessController {

    private final UploadSessionRepository uploadSessionRepository;
    private final CommandExecutorService commandExecutorService;

    @Autowired
    public ProcessController(UploadSessionRepository uploadSessionRepository, CommandExecutorService commandExecutorService) {
        this.uploadSessionRepository = uploadSessionRepository;
        this.commandExecutorService = commandExecutorService;
    }

    @GetMapping("/process/{id}")
    public String process(@PathVariable Long id, Model model) {
        // Start the command execution asynchronously
        if (!commandExecutorService.isProcessRunning()) {
            commandExecutorService.executeCommands(id, uploadSessionRepository);
        }

        // Return the log page immediately
        model.addAttribute("processId", id);
        model.addAttribute("log", "Initializing logs...");
        return "log"; // JSP page for logs
    }

    @PostMapping("/return")
    public String returnProcess() {
        commandExecutorService.killProcess();
        try {
            Thread.sleep(3000); // Delay for 3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        return "redirect:/dashboard"; // Redirect to dashboard page
    }

    @PostMapping("/stop")
    public String stopProcess(@RequestParam("processId") Long processId, Model model) {
        commandExecutorService.killProcess(); // Stop the process
        model.addAttribute("processId", processId);
        model.addAttribute("log", "Process stopped by user. Logs will persist.");
        return "log";
    }

}