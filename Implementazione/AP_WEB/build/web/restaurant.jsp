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
            <%for (String data : (String[]) request.getAttribute("info")) {
                    out.println(data+"<br>");
            }%>
    </body>
</html>
