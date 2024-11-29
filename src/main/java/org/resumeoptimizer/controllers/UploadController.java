package org.resumeoptimizer.controllers;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.resumeoptimizer.entities.UploadSession;
import org.resumeoptimizer.entities.User;
import org.resumeoptimizer.repositories.UploadSessionRepository;
import org.resumeoptimizer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Objects;

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
        String home = System.getProperty("user.home");
        String resumeMatcherDir = "ats/Resume-Matcher/Data";
        String userDir = "users";
        // Check if user is found
        String userFolder;
        if (Objects.equals(user.getRole(), "USER")) {
            userFolder = "uploads/" + user.getId();
        } else {
            // Use guest username as fallback
            userFolder = "uploads/" + user.getUsername();
        }
        String epochStr = String.valueOf((int) (System.currentTimeMillis() / 1000));
        String fullPath = home + "/" + resumeMatcherDir + "/" + userDir + "/" + userFolder + "/" + epochStr;

        new File(fullPath).mkdirs();

        // Save files
        String resumeFileName = resume.getOriginalFilename();
        String jobDescFileName = jobDesc.getOriginalFilename();

        // Copy to storage directory
        resume.transferTo(new File(fullPath + "/" + resumeFileName));
        jobDesc.transferTo(new File(fullPath + "/" + jobDescFileName));

        String resumesPath = home + "/" + resumeMatcherDir + "/Resumes";
        String jobDescPath = home + "/" + resumeMatcherDir + "/JobDescription";

        // Remove all files in resumesPath and jobDescPath
        try {
            FileUtils.cleanDirectory(new File(resumesPath));
            FileUtils.cleanDirectory(new File(jobDescPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transfer the resume file to the resumesPath
        resume.transferTo(new File(resumesPath + "/" + resumeFileName));
        // Transfer the job description file to the jobDescPath
        jobDesc.transferTo(new File(jobDescPath + "/" + jobDescFileName));

        // Save upload session info
        UploadSession session = new UploadSession();
        session.setResumeFileName(resumeFileName);
        session.setJobDescFileName(jobDescFileName);
        session.setFolderPath(fullPath);
        session.setUserId(user.getId());
        session.setUsername(user.getUsername());
        session.setUserRole(user.getRole());
        session.setEpoch(System.currentTimeMillis());
        session.setScore(0.0);
        uploadSessionRepository.save(session);

        // Redirect to processing page or result page
        return "redirect:/process/" + session.getId();
    }
}
