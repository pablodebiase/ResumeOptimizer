<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Resume Optimizer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .login-container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 500px;
        }
        .banner {
            text-align: center;
            margin-bottom: 20px;
        }
        .banner h1 {
            color: #333;
            font-size: 2em;
            margin: 0;
        }
        form {
            display: flex;
            flex-direction: column;
        }
        input[type="text"],
        input[type="password"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
        }
        button[type="submit"] {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        button[type="submit"]:hover {
            background-color: #0056b3;
        }
        .links {
            text-align: center;
            margin-top: 20px;
        }
        .links a {
            margin: 0 10px;
            text-decoration: none;
            color: #007bff;
            font-weight: bold;
            transition: color 0.3s;
        }
        .links a:hover {
            color: #0056b3;
        }
    </style>
</head>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); %>
<% response.setHeader("Pragma", "no-cache"); %>
<% response.setDateHeader("Expires", 0); %>

<body>
    <div class="login-container">
        <div class="banner">
            <h1>Resume Optimizer</h1>
        </div>
        <c:if test="${not empty error}">
            <p style="color:red;">${error}</p>
        </c:if>
        <form action="/login" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="text" name="username" placeholder="Username" value="${user.username}" required/>
            <input type="password" name="password" placeholder="Password" value="${user.password}" required/>
            <button type="submit">Login</button>
        </form>
        <div class="links">
            <a href="/register" style="margin-right: 50px;">Register</a>
            <a href="/guest">Continue as Guest</a>
        </div>
    </div>
</body>
</html>
