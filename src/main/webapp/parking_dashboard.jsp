<%-- 
    Document   : parking_dashboard
    Created on : 24 abr 2026, 3:18:59 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- IMPORTANTE: Sin esta línea el c:forEach no funcionará --%>
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

        <div class="container" style="max-width: 900px; margin-top: 20px;">
            <table border="1" id="table" style="width: 100%; border-collapse: collapse;">
                <tr id="encabezado">
                    <th>Nombre del Parqueo</th>
                    <th>Capacidad Total</th>
                    <th>Espacios Ocupados</th>
                    <th>Disponibles</th>
                    <th style="width: 250px;">Estado Visual</th>
                </tr>
                
                <c:forEach var="p" items="${report}">
                    <tr>
                        <td><strong><c:out value="${p.name}"/></strong></td>
                        <td style="text-align: center;">${p.numberOfSpaces}</td>
                        <td style="text-align: center;">${p.occupatedSpaces}</td>
                        <td style="text-align: center;">
                            ${p.numberOfSpaces - p.occupatedSpaces}
                        </td>
                        <td style="padding: 15px;">
                            <%-- Cálculo del porcentaje de ocupación --%>
                            <c:set var="porcentaje" value="${(p.occupatedSpaces * 100) / p.numberOfSpaces}" />
                            
                            <div style="width: 100%; background: #34495e; border-radius: 5px; height: 12px; overflow: hidden;">
                                <div style="width: ${porcentaje}%; 
                                            background: ${p.occupatedSpaces >= p.numberOfSpaces ? '#e74c3c' : '#2ecc71'}; 
                                            height: 100%; transition: width 0.5s;">
                                </div>
                            </div>
                            <small style="display: block; margin-top: 5px; color: #bdc3c7;">
                                ${porcentaje}% ocupado
                            </small>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            
            <div style="margin-top: 30px;">
                <a href="main_menu.html" class="cancel" style="text-decoration: none; padding: 10px 20px;">Volver al Menú</a>
            </div>
        </div>
    </body>
</html>