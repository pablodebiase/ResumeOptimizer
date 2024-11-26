<%--
  Created by IntelliJ IDEA.
  User: hongo
  Date: 2024-11-25
  Time: 10:15 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Processing</title>
    <style>
        body {
            margin: 0;
            display: flex;
            flex-direction: column;
            height: 100vh;
        }
        .frame-container {
            flex: 1;
            display: flex;
            flex-direction: column;
        }
        .upper-frame {
            flex: 3;
            border: none;
        }
        .lower-frame {
            flex: 1;
            border: none;
        }
    </style>
</head>
<body>
    <div class="frame-container">
        <iframe class="upper-frame" src="${streamlitUrl}"></iframe>
        <iframe class="lower-frame" src="${pageContext.request.contextPath}/log-output"></iframe>
    </div>
</body>
</html>
