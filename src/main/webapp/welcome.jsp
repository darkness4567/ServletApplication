<%@ page import="jakarta.servlet.http.HttpSession" %>
<%
    HttpSession sessionVar = request.getSession(false);
    if (sessionVar == null || sessionVar.getAttribute("username") == null) {
        System.out.println("No session found! Redirecting to login.");
        response.sendRedirect("login.jsp");
    } else {
        System.out.println("Session found! Welcome, " + sessionVar.getAttribute("username"));
    }
%>



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome</title>
</head>
<body>
<h2>Welcome, <%= session.getAttribute("username") %>!</h2>
<p>You have successfully logged in.</p>
<form action="logout" method="POST">
    <button type="submit">Logout</button>
</form>
</body>
</html>