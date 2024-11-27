package org.resumeoptimizer.services;

import org.resumeoptimizer.handlers.LogWebSocketHandler;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CommandExecutorService {

    private final LogWebSocketHandler logWebSocketHandler;

    public CommandExecutorService(LogWebSocketHandler logWebSocketHandler) {
        this.logWebSocketHandler = logWebSocketHandler;
    }

    @Async
    public void executeCommands(Long id) {
        String home = System.getProperty("user.home");
        String[] commands = {
                "cd " + home + "/ats/Resume-Matcher",
                "venv/bin/python run_first.py",
                "venv/bin/streamlit run streamlit_app.py --server.headless true --server.enableCORS false --server.enableXsrfProtection false"
        };

        try {
            Process process = executeCommand(commands);
            logOutput(process);
        } catch (IOException | InterruptedException e) {
            logWebSocketHandler.broadcast("Error executing commands: " + e.getMessage());
        }
    }

    private Process executeCommand(String[] commands) throws IOException {
        String combinedCommand = String.join(" && ", commands);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", combinedCommand);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private void logOutput(Process process) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line); // Log to terminal
            logWebSocketHandler.broadcast(line + "\n"); // Send logs to WebSocket clients

            if (line.contains("You can now view your Streamlit app in your browser")) {
                openBrowser("http://localhost:8501");
            }
        }

        process.waitFor();
    }

    private void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else if (os.contains("nix") || os.contains("nux")) {
                new ProcessBuilder("xdg-open", url).start();
            }
            System.out.println("Streamlit app opened in browser: " + url);
        } catch (IOException e) {
            LoggerFactory.getLogger(CommandExecutorService.class).error("Error opening browser", e);
        }
    }

}
