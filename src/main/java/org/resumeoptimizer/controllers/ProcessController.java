package org.resumeoptimizer.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

@Controller
public class ProcessController {

    @GetMapping("/process/{id}")
    public String process(@PathVariable Long id, Model model) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder();
                String home = System.getProperty("user.home");
                builder.command("bash", "-c", String.join(" && ",
                        "cd " + home + "/ats/Resume-Matcher",
                        "venv/bin/python run_first.py",
                        "venv/bin/streamlit run streamlit_app.py --server.headless true --server.enableCORS false --server.enableXsrfProtection false"));
                builder.redirectErrorStream(true);
                // Redirect output to a log file
                File logFile = new File("/tmp/resume-optimizer.log");
                builder.redirectOutput(logFile);
                builder.redirectErrorStream(true);

                // Start the process
                builder.start();
            } catch (Exception e) {
                LoggerFactory.getLogger(UploadController.class).error("Error while executing command", e);
            }
        });

        // Redirect the user to the log streaming page
        return "redirect:/log-stream";
    }
}
    /*
                Process process = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                boolean streamlitReady = false;

                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Log output to console

                    // Check for the specific text indicating Streamlit is ready
                    if (line.contains("You can now view your Streamlit app in your browser")) {
                        streamlitReady = true;
                        break; // Stop waiting once the text is found
                    }
                }

                // Wait for the process to finish or handle gracefully
                process.waitFor();

                if (!streamlitReady) {
                    System.err.println("Streamlit app did not start properly.");
                }
            } catch (Exception e) {
        LoggerFactory.getLogger(UploadController.class).error("Error while executing command", e);
            }
        });

        // Set Streamlit URL in the model
        model.addAttribute("streamlitUrl", "http://localhost:8501");
        return "process"; // Redirect to JSP page
    }
}
*/