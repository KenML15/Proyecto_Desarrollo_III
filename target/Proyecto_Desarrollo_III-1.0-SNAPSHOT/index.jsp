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
        <meta http-equiv="refresh" content="0;URL='main_menu.html'" />
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <%
            response.sendRedirect("login.jsp");
        %>

        <div class="redirect-fallback">
            <p>Redirigiendo al menú principal...</p>
            <p>Si no eres redirigido, <a href="menu_admin.jsp">haz clic aquí</a>.</p>
        </div>
    </body>
</html>
