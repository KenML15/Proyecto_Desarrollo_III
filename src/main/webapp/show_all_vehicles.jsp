<%-- 
    Document   : show_all_vehicles
    Created on : 10 abr 2026
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Vehículos</title>
    </head>
    <body>
        <div id="titulo">
            <h2>Vehículos Registrados</h2>
        </div>

        <div class="container" style="max-width: 800px;">
            <table border="1" id="table" style="width: 100%; border-collapse: collapse;">
                <tr id="encabezado">
                    <th>Placa</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Dueño</th> <th>Acciones</th>
                </tr>
                <c:forEach var="v" items="${vehicles}">
                    <tr>
                        <td><c:out value="${v.plate}"/></td>
                        <td><c:out value="${v.brand}"/></td>
                        <td><c:out value="${v.model}"/></td>
                        <td><c:out value="${v.color}"/></td>
                        <td>
                            <%-- Mostramos el nombre del dueño o un mensaje si es nulo --%>
                            <c:choose>
                                <c:when test="${not empty v.ownerName}">
                                    <c:out value="${v.ownerName}"/>
                                </c:when>
                                <c:otherwise>
                                    <span style="color: gray; font-style: italic;">Sin asignar</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="vehicles?action=edit&plate=${v.plate}" class="save" style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;">Editar</a>
                            <a href="vehicles?action=delete&plate=${v.plate}" class="cancel" style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;" onclick="return confirm('¿Seguro que desea eliminar el vehículo ${v.plate}?')">Eliminar</a>
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