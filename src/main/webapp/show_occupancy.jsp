<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Ocupación en Tiempo Real</title>
    </head>
    <body>
        <div id="titulo">
            <c:choose>
                <c:when test="${not empty lotId}">
                    <h2>Ocupación Actual — ${activeAssignments[0].lotName}</h2>
                </c:when>
                <c:otherwise>
                    <h2>Vehículos en el Parqueo</h2>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="container container--wide">
            <table border="1" id="table">
                <tr id="encabezado">
                    <th>Placa del Vehículo</th>
                    <th>Ubicación (Parqueo)</th>
                    <th>Hora de Entrada</th>
                    <th>Estado Actual</th>
                    <th>Acciones</th>
                </tr>
                <c:forEach var="a" items="${activeAssignments}">
                    <tr>
                        <td><strong><c:out value="${a.plateVehicle}"/></strong></td>
                        <td><c:out value="${a.lotName}"/></td>
                        <td><c:out value="${a.entryTime}"/></td>
                        <td>
                            <span class="badge-parked">PARQUEADO</span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/assignments?action=release&id=${a.id}"
                               class="cancel btn-table"
                               onclick="return confirm('¿Confirmar salida del vehículo ${a.plateVehicle}?')">
                                Registrar Salida
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br>
            <c:choose>
                <c:when test="${not empty lotId}">
                    <a href="parkingLot" class="cancel">← Volver a Gestionar Parqueos</a>
                </c:when>
                <c:otherwise>
                    <a href="main_menu.html" class="cancel">Volver al Menú</a>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
