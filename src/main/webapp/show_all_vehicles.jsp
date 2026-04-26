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

        <div class="container container--medium">
            <table border="1" id="table">
                <tr id="encabezado">
                    <th>Placa</th>
                    <th>Marca</th>
                    <th>Modelo</th>
                    <th>Color</th>
                    <th>Dueño</th>
                    <th>Acciones</th>
                </tr>
                <c:forEach var="v" items="${vehicles}">
                    <tr>
                        <td><c:out value="${v.plate}"/></td>
                        <td><c:out value="${v.brand}"/></td>
                        <td><c:out value="${v.model}"/></td>
                        <td><c:out value="${v.color}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty v.ownerName}">
                                    <c:out value="${v.ownerName}"/>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-unassigned">Sin asignar</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="vehicles?action=edit&plate=${v.plate}" class="save btn-table">Editar</a>
                            
                            <a href="vehicles?action=delete&plate=${v.plate}" class="cancel btn-table"
                               onclick="return confirm('¿Seguro que desea eliminar el vehículo ${v.plate}?')">Eliminar</a>
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
