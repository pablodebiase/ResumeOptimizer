package org.resumeoptimizer.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;

/*
import java.io.InputStreamReader;
@Controller
public class LogController {

    @GetMapping("/log-output")
    public void streamLog(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        ProcessBuilder logProcess = new ProcessBuilder("tail", "-f", "/tmp/resume-optimizer.log"); // Adjust as necessary
        Process process = logProcess.start();
        try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
             var writer = response.getWriter()) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line + "\n");
                writer.flush();
            }
        }
    }
}
*/

@Controller
public class LogController {

    private static final String LOG_FILE_PATH = "/tmp/resume-optimizer.log"; // Update this path
    private static final String TRIGGER_PHRASE = "You can now view your Streamlit app in your browser";

    @GetMapping("/log-stream")
    public void streamLog(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        try (BufferedReader logReader = new BufferedReader(new FileReader(LOG_FILE_PATH));
             PrintWriter writer = response.getWriter()) {
            String line;
            while ((line = logReader.readLine()) != null) {
                writer.println(line);
                writer.flush();

                // Check for the trigger phrase
                if (line.contains(TRIGGER_PHRASE)) {
                    writer.println("\n--- Redirecting to Streamlit app ---");
                    response.flushBuffer();

                    // Introduce a small delay before redirecting
                    Thread.sleep(1000);
                    response.sendRedirect("http://localhost:8501");
                    return;
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(UploadController.class).error("Error while executing command", e);
        }
    }
}
