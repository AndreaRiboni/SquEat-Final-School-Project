<%-- 
    Document   : restaurant
    Created on : 26-feb-2019, 12.23.44
    Author     : saponaro.andrea
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>INFO LOCALE</title>
    </head>
    <body>
        <h2>Info Locale</h2>
            <% String[] data = (String[]) request.getAttribute("info");
                    out.println("Nome: "+data[1]+"<br>");
                    out.println("Lougo: "+data[2]+"<br>");
                    out.println("Stelle: "+data[3]+"<br>");
                    out.println("Cellulare: "+data[4]+"<br>");
                    out.println("Menù: "+data[5]+"<br>");
                    request.setAttribute("menù", data[5]);
                    out.println("<a href='menù.jsp'>Menù</a>");

            %>
    </body>
</html>
