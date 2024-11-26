package org.resumeoptimizer.websocket;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WebSocketServer {
    private final Map<Long, WebSocketSession> sessions = new HashMap<>();

    public void sendLogOutput(InputStream inputStream, Long id) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            WebSocketSession session = new WebSocketSession();
            while ((line = reader.readLine()) != null) {
                if (session != null) {
                    session.sendMessage(new TextMessage(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error sending log output to client: " + e.getMessage());
        }
    }
}