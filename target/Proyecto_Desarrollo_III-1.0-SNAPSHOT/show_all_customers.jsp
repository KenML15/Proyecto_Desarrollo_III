<%--
    Document   : show_all_customer
    Created on : 12 abr 2026, 10:23:49 p. m.
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

        <div class="container container--wide">
            <table border="1" id="table">
                <tr id="encabezado">
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Cédula</th>
                    <th>Teléfono</th>
                    <th>Correo</th>
                    <th>Discapacidad</th>
                    <th>Acciones</th>
                </tr>
                <c:forEach items="${customers}" var="c">
                    <tr>
                        <td><c:out value="${c.id}"/></td>
                        <td><c:out value="${c.name}"/></td>
                        <td><c:out value="${c.cedula}"/></td>
                        <td><c:out value="${c.telefono}"/></td>
                        <td><c:out value="${c.correo}"/></td>
                        <td><c:out value="${c.disabilityPresented ? 'Sí' : 'No'}"/></td>
                        <td>
                            <a href="customers?action=edit&id=${c.id}" class="save btn-table">Editar</a>
                            <a href="customers?action=delete&id=${c.id}" class="cancel btn-table"
                               onclick="return confirm('¿Eliminar cliente?')">Eliminar</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="footer-nav">
            <a href="main_menu.html" class="cancel">Volver al menú</a>
        </div>
    </body>
</html>
