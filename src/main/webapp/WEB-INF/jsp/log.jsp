<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resume Matcher Log</title>
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

        .button-container {
            margin-top: 10px;
            display: flex;
            justify-content: flex-start;
            gap: 10px;
        }

        button {
            padding: 10px 20px;
            font-size: 16px;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .stop-button {
            background-color: red;
        }

        .stop-button:hover {
            background-color: darkred;
        }

        .return-button {
            background-color: orange;
        }

        .return-button:hover {
            background-color: darkorange;
        }

        span {
            display: inline;
        }
    </style>
</head>
<body>
<h1>Resume Matcher Log</h1>
<pre id="log"></pre>
<div class="button-container">
    <form id="stopForm" method="POST" action="/stop">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="processId" value="${processId}"/>
        <button class="stop-button" type="submit" onclick="event.preventDefault(); stopProcess();">Stop</button>
    </form>
    <form id="returnForm" method="POST" action="/return">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="return-button" type="submit">Return</button>
    </form>
</div>
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

    function stopProcess() {
        const processId = document.querySelector('input[name="processId"]').value;
        fetch('/stop', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': '${_csrf.token}'
            },
            body: `processId=${processId}`
        })
            .then(response => {
                if (response.ok) {
                    logElement.innerHTML += '<p style="color: red;">Process stopped by user.</p>';
                } else {
                    logElement.innerHTML += '<p style="color: red;">Failed to stop the process.</p>';
                }
            })
            .catch(error => {
                logElement.innerHTML += '<p style="color: red;">Error stopping the process: ' + error + '</p>';
            });
    }
</script>
</body>
</html>
