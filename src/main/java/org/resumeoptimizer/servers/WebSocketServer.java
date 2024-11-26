package org.resumeoptimizer.servers;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketServer extends TextWebSocketHandler {

    // Store active WebSocket sessions
    private final ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Register the session, assuming the client sends the ID as a query parameter
        String idParam = session.getUri().getQuery().replace("id=", "");
        Long id = Long.valueOf(idParam);
        sessions.put(id, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Handle incoming messages if needed
        session.sendMessage(new TextMessage("Server received: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // Remove the session on disconnect
        sessions.values().remove(session);
    }

    public void sendLineToLogOutput(String line, Long id) throws IOException {
        // Find the session for the given ID
        WebSocketSession session = sessions.get(id);
        if (session == null || !session.isOpen()) {
            return;
        }

        // Send the log output to the client
        session.sendMessage(new TextMessage(line));
    }

    public void sendLogOutput(InputStream inputStream, Long id) {
        // Find the session for the given ID
        WebSocketSession session = sessions.get(id);
        if (session == null || !session.isOpen()) {
            return;
        }

        // Stream the log output to the client
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    session.sendMessage(new TextMessage(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
