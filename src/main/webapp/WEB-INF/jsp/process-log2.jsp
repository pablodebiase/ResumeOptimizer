<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Process Logs</title>
</head>
<body>
    <h1>Process Logs</h1>
    <pre id="logs"></pre>

    <script>
        const logsElement = document.getElementById('logs');
        const id = /* Insert the ID dynamically here */;

        const socket = new WebSocket(`ws://localhost:8080/ws/logs?id=${id}`);

        socket.onmessage = (event) => {
            logsElement.textContent += event.data + '\n';
        };

        socket.onclose = () => {
            logsElement.textContent += '\nConnection closed.';
        };

        socket.onerror = (error) => {
            logsElement.textContent += '\nAn error occurred: ' + error.message;
        };
    </script>
</body>
</html>
