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
    <title>Process Logs</title>
    <style>
    #log {
        font-family: monospace;
        white-space: pre-wrap;
        background-color: #1e1e1e;
        color: #c0c0c0;
        padding: 10px;
        overflow-y: auto;
        height: 80vh;
        border: 1px solid #444;
        border-radius: 5px;
    }
    span {
            display: inline;
        }
</style>

</head>
<body>
    <h1>Process Logs</h1>
    <pre id="log"></pre>

    <script>
    const logElement = document.getElementById('log');
    const socket = new WebSocket('ws://localhost:8080/logs');

    socket.onmessage = function (event) {
        const logLine = event.data; // Expecting HTML-formatted string
        logElement.innerHTML += logLine; // Use innerHTML to render styles
        logElement.scrollTop = logElement.scrollHeight; // Auto-scroll to bottom
    };

    socket.onclose = function () {
        logElement.innerHTML += '<p style="color: red;">Connection closed.</p>';
    };
    </script>
</body>
</html>
