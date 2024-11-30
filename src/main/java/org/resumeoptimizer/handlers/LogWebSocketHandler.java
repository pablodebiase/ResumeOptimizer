package org.resumeoptimizer.handlers;

import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogWebSocketHandler extends TextWebSocketHandler {
    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessions.remove(session);
    }

    public void broadcast(String message) {
        String htmlMessage = convertAnsiToHtml(message);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(htmlMessage));
            } catch (IOException e) {
                LoggerFactory.getLogger(LogWebSocketHandler.class).error("Error sending message to WebSocket session", e);
            }
        }
    }

    public String convertAnsiToHtml(String ansiText) {
        // Pattern for ANSI escape codes
        String ansiRegex = "\u001b\\[([0-9;]*)m";
        Pattern pattern = Pattern.compile(ansiRegex);
        Matcher matcher = pattern.matcher(ansiText);

        // Replace ANSI codes with appropriate HTML styles
        StringBuilder htmlBuilder = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            // Append text before the ANSI code
            htmlBuilder.append(ansiText, lastEnd, matcher.start());

            // Detect color and apply styles
            String ansiCode = matcher.group(1);
            String style = getStyleForAnsiCode(ansiCode);
            if (style != null) {
                htmlBuilder.append("<span style=\"").append(style).append("\">");
            } else if ("0".equals(ansiCode)) {
                // Close span if code resets styling
                htmlBuilder.append("</span>");
            }
            lastEnd = matcher.end();
        }

        // Append remaining text
        htmlBuilder.append(ansiText.substring(lastEnd));

        // Replace newline characters with <br> tags for HTML rendering
        return htmlBuilder.toString().replace("\n", "<br>");
    }


    private String getStyleForAnsiCode(String ansiCode) {
        String[] codes = ansiCode.split(";");
        StringBuilder styleBuilder = new StringBuilder();

        for (String code : codes) {
            switch (code) {
                case "30":
                    styleBuilder.append("color: black;");
                    break;
                case "31":
                    styleBuilder.append("color: red;");
                    break;
                case "32":
                    styleBuilder.append("color: green;");
                    break;
                case "33":
                    styleBuilder.append("color: yellow;");
                    break;
                case "34":
                    styleBuilder.append("color: blue;");
                    break;
                case "35":
                    styleBuilder.append("color: magenta;");
                    break;
                case "36":
                    styleBuilder.append("color: cyan;");
                    break;
                case "37":
                    styleBuilder.append("color: white;");
                    break;
                case "1":
                    styleBuilder.append("font-weight: bold;");
                    break;
                case "0":
                    styleBuilder.append("");
                    break;
                default:
                    styleBuilder.append("");
                    break;
            }
        }
        return !styleBuilder.isEmpty() ? styleBuilder.toString() : null;
    }
}
