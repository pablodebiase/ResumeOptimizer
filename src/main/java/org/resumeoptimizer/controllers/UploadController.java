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
        if (user != null) {
            userFolder = "uploads/" + user.getId();
        } else {
            // Use guest username as fallback
            String guestUsername = userService.getGuestUsername();
            userFolder = "uploads/" + guestUsername;
            user = new User();
            user.setUsername(guestUsername);
            user.setRole("GUEST");
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

        /*
        Optional<UploadSession> session2 = uploadSessionRepository.findById(session.getId());
        if (session2.isPresent()) {
            session2.get().setScore(45.0);
            uploadSessionRepository.save(session2.get());
        }
        */

        // Redirect to processing page or result page
        return "redirect:/process/" + session.getId();
    }
/*
    @GetMapping("/process/{id}")
    public String process(@PathVariable Long id) {
        // Create a new thread to execute the commands
        // Execute the commands
        String home = System.getProperty("user.home");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("sh", "-c", String.join(" && ",
                "cd " + home + "/ats/Resume-Matcher",
                "venv/bin/python run_first.py",
                "venv/bin/streamlit run streamlit_app.py --server.headless true"));

        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            WebSocketServer webSocketServer = new WebSocketServer();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                webSocketServer.sendLineToLogOutput(line, id);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            LoggerFactory.getLogger(UploadController.class).error("Error while executing command", e);
        }

            //Process process = Runtime.getRuntime().exec(new String[]{
            //        "cd", home + "/ats/Resume-Matcher",
            //        "venv/bin/python", "run_first.py",
            //        "venv/bin/streamlit", "run", "streamlit_app.py", "--server.headless", "true"
            //});

            //// Send log output to the client
            //WebSocketServer webSocketServer = new WebSocketServer();
            //webSocketServer.sendLogOutput(process.getInputStream(), id);

            //// Wait for the process to finish
            //process.waitFor();

        // Return the HTML page to display the log output
        return "process-log";
    }
    */
}
