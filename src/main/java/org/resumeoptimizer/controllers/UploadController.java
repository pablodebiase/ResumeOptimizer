package org.resumeoptimizer.controllers;

import org.resumeoptimizer.entities.UploadSession;
import org.resumeoptimizer.entities.User;
import org.resumeoptimizer.services.UserService;
import org.resumeoptimizer.repositories.UploadSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Controller
public class UploadController {

    private final UploadSessionRepository uploadSessionRepository;
    private final UserService userService;

    @Autowired
    public UploadController(UploadSessionRepository uploadSessionRepository, UserService userService) {
        this.uploadSessionRepository = uploadSessionRepository;
        this.userService = userService;
    }

    @GetMapping("/upload")
    public String showUploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("resume") MultipartFile resume,
                                   @RequestParam("jobDesc") MultipartFile jobDesc,
                                   Principal principal) throws IOException {

        // Get current user
        User user = userService.findByUsername(principal.getName());

        // Create user-specific folder
        String userFolder = "uploads/" + user.getId();
        new File(userFolder).mkdirs();

        // Save files
        String resumeFileName = resume.getOriginalFilename();
        String jobDescFileName = jobDesc.getOriginalFilename();

        resume.transferTo(new File(userFolder + "/" + resumeFileName));
        jobDesc.transferTo(new File(userFolder + "/" + jobDescFileName));

        // Save upload session info
        UploadSession session = new UploadSession();
        session.setResumeFileName(resumeFileName);
        session.setJobDescFileName(jobDescFileName);
        session.setFolderPath(userFolder);
        session.setUser(user);
        uploadSessionRepository.save(session);

        // Redirect to processing page or result page
        return "redirect:/process/" + session.getId();
    }
}
