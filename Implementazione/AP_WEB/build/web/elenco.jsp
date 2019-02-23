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
        <%
            String[] list = request.getParameter("elenco");
            for (int i = 0; i < request.getParameter("elenco").length(); i++) {
                
            }
            String[] authors = request.getParameterValues("R");
            if (authors != null) {
        %>
        <h3>You have selected author(s):</h3>
        <ul>
            <%
                for (int i = 0; i < authors.length; ++i) {
            %>
            <li><%= authors[i]%></li>
                <%
                    }
                %>
        </ul>
        <a href="<%= request.getRequestURI()%>">BACK</a>
        <%
            }
        %>
    </body>
</html>
