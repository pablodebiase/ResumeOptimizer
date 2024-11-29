<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Dashboard</title>
    <style>
        /* General Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .dashboard-container {
            text-align: center;
            background: #ffffff;
            padding: 30px 20px;
            border-radius: 10px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }

        h1 {
            font-size: 1.8rem;
            margin-bottom: 20px;
            color: #007bff;
        }

        .button-container {
            margin-top: 20px;
        }

        button {
            padding: 12px 20px;
            font-size: 1rem;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s ease, box-shadow 0.3s ease;
            width: 100%;
            margin: 10px 0;
        }

        .upload-button {
            background-color: #007bff;
            color: #fff;
        }

        .scores-button {
            background-color: #28a745;
            color: #fff;
        }

        .logout-button {
            background-color: #dc3545;
            color: #fff;
        }

        button:hover {
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .upload-button:hover {
            background-color: #0056b3;
        }

        .scores-button:hover {
            background-color: #1e7e34;
        }

        .logout-button:hover {
            background-color: #c82333;
        }

        .button-container form {
            margin: 0;
        }
    </style>
</head>
<body>
<div class="dashboard-container">
    <h1>Dashboard</h1>
    <div class="button-container">
        <form action="/upload" method="GET">
            <button class="upload-button" type="submit">Upload & Process Page</button>
        </form>
        <form action="/scores" method="GET">
            <button class="scores-button" type="submit">View Scores Log</button>
        </form>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button type="submit" class="logout-button">Logout</button>
        </form>
    </div>
</div>
</body>
</html>
