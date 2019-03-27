<%-- 
    Document   : menù
    Created on : 27-mar-2019, 22.54.22
    Author     : andreasaponaro
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Menù Locale</title>
    </head>
    <body>
        <h2>Menù Locale</h2>
        <% String data = (String) request.getAttribute("menù");
        out.println("Nome: "+data+"<br>");
        %>
    </body>
</html>
