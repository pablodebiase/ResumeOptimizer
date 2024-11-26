<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Process Log</title>
    <style>
        #log-window {
            width: 800px;
            height: 600px;
            border: 1px solid #ccc;
            padding: 10px;
            overflow-y: scroll;
        }
    </style>
</head>
<body>
    <h1>Process Log</h1>
    <div id="log-window"></div>
    <script>
        const logWindow = document.getElementById('log-window');

        // Create a WebSocket connection to receive log output
        const socket = new WebSocket(`ws://localhost:8080/process/${id}/log`);

        // Handle incoming log messages
        socket.onmessage = (event) => {
            const logMessage = event.data;
            logWindow.innerHTML += `<pre>${logMessage}</pre>`;
            logWindow.scrollTop = logWindow.scrollHeight;
        };

        // Handle connection close
        socket.onclose = () => {
            logWindow.innerHTML += '<p>Process finished.</p>';
            // Open a new tab to http://localhost:8501
            window.open('http://localhost:8501', '_blank');
        };

        // Handle errors
        socket.onerror = (error) => {
            logsElement.textContent += '\nAn error occurred: ' + error.message;
        };

    </script>
</body>
</html>