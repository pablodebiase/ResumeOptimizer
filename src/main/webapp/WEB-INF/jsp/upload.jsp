<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Upload - Resume Optimizer</title>
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

        .upload-container {
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

        .upload-instructions {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
            text-align: center;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        input[type="file"] {
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
            width: 100%;
        }

        .file-upload-container {
            margin-bottom: 20px;
        }

        .file-upload-container div {
            margin-bottom: 20px;
        }

        .file-upload-container h3 {
            color: #444;
            margin-bottom: 10px;
        }

        .submit-button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .submit-button:hover {
            background-color: #0056b3;
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

        .logout-button {
            position: relative;
            top: -300px;
            right: -550px;
            background-color: #ff4c4c;
            color: white;
            border: none;
            padding: 5px 10px;
            font-size: 1em;
            cursor: pointer;
            border-radius: 5px;
        }

        .logout-button:hover {
            background-color: #e60000;
        }
    </style>
</head>
<body>
<!-- Logout Button Outside the Main Container -->
<form action="/logout" method="post">
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <button type="submit" class="logout-button">Logout</button>
</form>
<div class="upload-container">
    <div class="banner">
        <h1>Resume Optimizer</h1>
    </div>
    <p class="upload-instructions">Upload your files in pdf format. Your resume will be scored with the job description
        to provide you with the best possible matches.</p>
    <form action="/upload" method="post" enctype="multipart/form-data">
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <div class="file-upload-container">
            <div>
                <h3>Resume</h3>
                <input type="file" name="resume" accept=".pdf" required/>
            </div>
            <div>
                <h3>Job Description</h3>
                <input type="file" name="jobDesc" accept=".pdf" required/>
            </div>
        </div>
        <button type="submit" class="submit-button">Upload</button>
    </form>
</div>
</body>
</html>
