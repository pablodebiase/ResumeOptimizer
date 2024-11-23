<!--
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resume Optimizer</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: linear-gradient(to bottom, #f0f2f5, #ffffff);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .home-container {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 800px;
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

        .description {
            font-size: 18px;
            color: #666;
            margin-bottom: 20px;
            text-align: center;
        }

        .start-button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 200px;
            margin: 0 auto;
            display: block;
        }

        .start-button:hover {
            background-color: #0056b3;
        }

        .start-button-container {
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .start-button {
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
            transition: background-color 0.3s;
            width: 200px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
<div class="home-container">
    <div class="banner">
        <h1>Resume Optimizer</h1>
    </div>
    <p class="description">Welcome to Resume Optimizer, a tool designed to help you increase your chances of passing the
        Applicant Tracking System (ATS) and getting your resume in front of the hiring manager. Our system compares your
        resume to the job description and provides a score, allowing you to optimize your resume and improve your
        chances of getting hired.</p>
    <div class="start-button-container">
        <a href="/login" class="start-button">Get Started</a>
    </div>
</div>
</body>
</html> -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resume Optimizer</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap">
    <style>
        body {
            font-family: 'Roboto', Arial, sans-serif;
            background: linear-gradient(to bottom, #f0f2f5, #ffffff);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .home-container {
            background-color: #ffffff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
            width: 100%;
            max-width: 800px;
            text-align: center;
        }

        .banner {
            margin-bottom: 30px;
        }

        .banner h1 {
            color: #333;
            font-size: 2.5em;
            margin: 0;
        }

        .description {
            color: #666;
            font-size: 1.2em;
            margin-top: 10px;
            margin-bottom: 30px;
        }

        .cta-button {
            background-color: #007bff;
            color: #ffffff;
            padding: 15px 30px;
            font-size: 1em;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .cta-button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="home-container">
    <div class="banner">
        <h1>Welcome to Resume Optimizer</h1>
        <p class="description">Welcome to Resume Optimizer, a tool designed to help you increase your chances of passing the
            Applicant Tracking System (ATS) and getting your resume in front of the hiring manager. Our system compares your
            resume to the job description and provides a score, allowing you to optimize your resume and improve your
            chances of getting hired.</p>
    </div>
    <a href="/login" class="cta-button">Get Started</a>
</div>
</body>
</html>
