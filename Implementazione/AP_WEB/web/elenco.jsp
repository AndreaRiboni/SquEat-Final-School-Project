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
            <%for (String place : (String[]) request.getAttribute("elenco")) {
                    String[] data = place.split(";");
                    out.println("<form action='getrestaurant' method='post'>");
                    out.println("Nome: " + data[0] + "<br>");
                    out.println(data[1] + "<br>");
                    out.println("Recensione: " + data[2] + "<br>");
                    out.println("<input name='ID' type='number' value="+data[3]+" hidden>");
                    out.println("<input type='submit' value='RICHIEDI INFO'><br><br>");
                    out.println("</form>");
            }%>
    </body>
</html>
