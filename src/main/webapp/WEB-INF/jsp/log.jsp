<%--
  Created by IntelliJ IDEA.
  User: hongo
  Date: 2024-11-26
  Time: 12:05 a.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Streaming Logs</title>
</head>
<body>
    <pre id="log-output">Loading logs...</pre>
    <script>
        // Continuously fetch logs
        async function fetchLogs() {
            const response = await fetch('/log-stream');
            const reader = response.body.getReader();
            const decoder = new TextDecoder("utf-8");
            const logOutput = document.getElementById("log-output");

            while (true) {
                const { value, done } = await reader.read();
                if (done) break;

                logOutput.textContent += decoder.decode(value);
                logOutput.scrollTop = logOutput.scrollHeight; // Auto-scroll to the bottom
            }
        }

        fetchLogs();
    </script>
</body>
</html>
