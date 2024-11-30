package org.resumeoptimizer.services;

import org.resumeoptimizer.entities.Session;
import org.resumeoptimizer.handlers.LogWebSocketHandler;
import org.resumeoptimizer.repositories.SessionRepository;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CommandExecutorService {

    private final LogWebSocketHandler logWebSocketHandler;
    private final AtomicReference<Process> runningProcess = new AtomicReference<>();

    public CommandExecutorService(LogWebSocketHandler logWebSocketHandler) {
        this.logWebSocketHandler = logWebSocketHandler;
    }

    public boolean isProcessRunning() {
        return runningProcess.get() != null;
    }

    @Async
    public void executeCommands(Long id, SessionRepository sessionRepository) {
        if (isProcessRunning()) {
            System.out.println("Process already running, skipping execution.");
            return;
        }

        String home = System.getProperty("user.home");
        String[] commands = {
                "cd " + home + "/ats/Resume-Matcher",
                "venv/bin/python run_first.py",
                "venv/bin/streamlit run streamlit_app.py --server.headless true --server.enableCORS false --server.enableXsrfProtection false"
        };

        try {
            Process process = executeCommand(commands);
            runningProcess.set(process);
            logOutput(process, id, sessionRepository);
        } catch (IOException e) {
            logWebSocketHandler.broadcast("Error executing commands: " + e.getMessage());
        } finally {
            runningProcess.set(null); // Clear when process finishes or is terminated
        }
    }

    private Process executeCommand(String[] commands) throws IOException {
        String combinedCommand = String.join(" && ", commands);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", combinedCommand);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private void logOutput(Process process, Long id, SessionRepository sessionRepository) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Log to terminal
                logWebSocketHandler.broadcast(line + "\n"); // Send logs to WebSocket clients

                if (line.contains("You can now view your Streamlit app in your browser")) {
                    System.out.println("Detected Streamlit URL phrase. Opening browser...");
                    openBrowser("http://localhost:8501");
                }

                if (!found && line.contains("Similarity Score:")) {
                    found = true;
                    processScore(line, id, sessionRepository);
                }
            }
        } catch (IOException e) {
            String message = "Stream closed or error reading logs: " + e.getMessage();
            System.err.println(message);
            logWebSocketHandler.broadcast(message);
        } finally {
            try {
                process.waitFor(); // Ensure process termination is handled
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Process wait interrupted: " + e.getMessage());
            }
            runningProcess.set(null); // Clear process reference
        }
    }

    private void processScore(String line, Long id, SessionRepository sessionRepository) {
        String[] parts = line.split("Similarity Score: ");
        if (parts.length > 1) {
            try {
                double score = Double.parseDouble(parts[1].trim());
                Optional<Session> session = sessionRepository.findById(id);
                if (session.isPresent()) {
                    session.get().setScore(score);
                    sessionRepository.save(session.get());
                }
            } catch (NumberFormatException e) {
                String message = "Failed parsing similarity score: " + e.getMessage();
                System.err.println(message);
                logWebSocketHandler.broadcast(message);
            }
        }
    }

    public void killProcess() {
        Process process = runningProcess.getAndSet(null);
        if (process != null) {
            process.destroy();
            String message = "Process terminated by user.";
            logWebSocketHandler.broadcast(message);
            System.out.println(message);
        } else {
            String message = "No process to terminate.";
            logWebSocketHandler.broadcast(message);
            System.out.println(message);
        }
    }

    private void openBrowser(String url) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "start", url).start();
            } else if (os.contains("mac")) {
                new ProcessBuilder("open", url).start();
            } else if (os.contains("nix") || os.contains("nux")) {
                // new ProcessBuilder("xdg-open", url).start();
                new ProcessBuilder("google-chrome", "--new-tab", url).start();
            }
            System.out.println("Streamlit app opened in browser: " + url);
        } catch (IOException e) {
            LoggerFactory.getLogger(CommandExecutorService.class).error("Error opening browser", e);
        }
    }
}
