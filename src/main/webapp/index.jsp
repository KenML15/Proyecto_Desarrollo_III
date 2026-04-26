<%--
    Document   : index
    Created on : 13 abr 2026
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cargando Sistema...</title>
        <%-- Redirección mediante Meta Tag (como respaldo) --%>
        <meta http-equiv="refresh" content="0;URL='main_menu.html'" />
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <%-- Redirección mediante Java Scriptlet (Ejecución inmediata en servidor) --%>
        <%
            response.sendRedirect("login.jsp");
        %>

        <div class="redirect-fallback">
            <p>Redirigiendo al menú principal...</p>
            <p>Si no eres redirigido, <a href="main_menu.html">haz clic aquí</a>.</p>
        </div>
    </body>
</html>
