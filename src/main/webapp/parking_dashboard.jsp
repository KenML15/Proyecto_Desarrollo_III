<%--
    Document   : parking_dashboard
    Created on : 24 abr 2026, 3:18:59 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Dashboard de Espacios</title>
    </head>
    <body>
        <div id="titulo">
            <h2>Estado de espacios por Parqueo</h2>
        </div>

        <div class="container container--wide">
            <table border="1" id="table">
                <tr id="encabezado">
                    <th>Nombre del Parqueo</th>
                    <th>Capacidad Total</th>
                    <th>Espacios Ocupados</th>
                    <th>Disponibles</th>
                    <th class="th-visual">Estado Visual</th>
                </tr>

                <c:forEach var="p" items="${report}">
                    <tr>
                        <td><strong><c:out value="${p.name}"/></strong></td>
                        <td class="td-center">${p.numberOfSpaces}</td>
                        <td class="td-center">${p.occupatedSpaces}</td>
                        <td class="td-center">
                            ${p.numberOfSpaces - p.occupatedSpaces}
                        </td>
                        <td class="td-visual">
                            <%-- Cálculo del porcentaje de ocupación --%>
                            <c:set var="porcentaje" value="${(p.occupatedSpaces * 100) / p.numberOfSpaces}" />

                            <div class="progress-bar">
                                <div class="${p.occupatedSpaces >= p.numberOfSpaces ? 'progress-bar__fill progress-bar__fill--full' : 'progress-bar__fill progress-bar__fill--ok'}"
                                     style="width: ${porcentaje}%;">
                                </div>
                            </div>
                            <small class="progress-label">
                                ${porcentaje}% ocupado
                            </small>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div class="footer-nav--lg">
                <a href="main_menu.html" class="cancel">Volver al Menú</a>
            </div>
        </div>
    </body>
</html>
