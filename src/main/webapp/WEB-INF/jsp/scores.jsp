<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: hongo
  Date: 2024-11-27
  Time: 11:40 p.m.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Scores</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            margin-top: 50px;
        }

        h1 {
            color: #333;
        }

        table {
            margin: 20px auto;
            border-collapse: collapse;
            width: 80%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f4f4f4;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #ddd;
        }

        .dashboard-button {
            display: block;
            margin: 20px auto;
            padding: 10px;
            background-color: #ffaa4c;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 1em;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h1>Your Scores</h1>
<table>
    <thead>
        <tr>
            <th>Date</th>
            <th>Resume</th>
            <th>Job Description</th>
            <th>Score</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="score" items="${scores}">
            <tr>
                <td>${score.date}</td>
                <td>${score.resume}</td>
                <td>${score.jobDesc}</td>
                <td>${score.value}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<form action="/dashboard" method="GET">
    <button type="submit" class="dashboard-button">Dashboard</button>
</form>

</body>
</html>