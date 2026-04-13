<%-- 
    Document   : index
    Created on : 13 abr 2026, 10:34:11 a.m.
    Author     : Kenneth
--%>

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
    </head>
    <body>
        <%-- Redirección mediante Java Scriptlet (Ejecución inmediata en servidor) --%>
        <%
            response.sendRedirect("main_menu.html");
        %>
        
        <div style="text-align: center; margin-top: 50px; font-family: Arial, sans-serif;">
            <p>Redirigiendo al menú principal...</p>
            <p>Si no eres redirigido, <a href="main_menu.html">haz clic aquí</a>.</p>
        </div>
    </body>
</html>
