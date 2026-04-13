<%-- 
    Document   : show_all_customer
    Created on : 12 abr 2026, 10:23:49 p. m.
    Author     : pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Clientes</title>
    </head>
    <body>
        <div id="titulo">
            <h2>Gestión de Clientes</h2>
        </div>

        <div class="container" style="max-width: 800px;">
            <table border="1" id="table" style="width: 100%; border-collapse: collapse;">
                <tr id="encabezado">
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Discapacidad</th>
                    <th>Acciones</th>
                </tr>
                <c:forEach items="${customers}" var="c">
                    <tr>
                        <td><c:out value="${c.id}"/></td>
                        <td><c:out value="${c.name}"/></td>
                        <td><c:out value="${c.disabilityPresented ? 'Sí' : 'No'}"/></td>
                        <td>
                            <a href="customers?action=edit&id=${c.id}" class="save" style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;">Editar</a>
                            <a href="customers?action=delete&id=${c.id}" class="cancel" style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;" onclick="return confirm('¿Eliminar cliente?')">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        
        <div style="margin-top: 20px;">
            <a href="main_menu.html" class="cancel" style="text-decoration: none; padding: 10px 20px;">Volver al menú</a>
        </div>
    </body>
</html>