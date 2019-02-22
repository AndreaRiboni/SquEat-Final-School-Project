<%-- 
    Document   : login
    Created on : 16-feb-2019, 11.52.24
    Author     : saponaro.andrea
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="gestione.js"></script>
        <title>Login Page</title>
    </head>
    <body>
        <h1>Login</h1>
        <form action="loginrequest" method="post">
            <input type="text" name="user" onchange="checkFormLogin()">
            <input type="text" name="psw" onchange="checkFormLogin()">
            <input type="submit" id="log" disabled>
        </form>
        <c:choose>
            <c:when test="${esito==TRUE}">
                pizza. 
                <br />
            </c:when>    
            <c:otherwise>
                pizzas. 
                <br />
            </c:otherwise>
        </c:choose>
        <a href="register.html">REGISTRATI</a>
    </body>
</html>

