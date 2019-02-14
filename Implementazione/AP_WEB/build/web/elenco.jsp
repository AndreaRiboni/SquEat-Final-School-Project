<%-- 
    Document   : elenco
    Created on : 12-feb-2019, 12.29.44
    Author     : saponaro.andrea
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Elenco Ristoranti</title>
    </head>
    <body>
        <h2>Elenco Locali</h2>
        <c:forEach var="place" items="${elenco}">
            <c:out value="${place}"/> 
        </c:forEach>
    </body>
</html>
