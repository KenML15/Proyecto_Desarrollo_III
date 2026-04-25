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
            <h2>Vehículos en el Parqueo</h2>
        </div>
        <div class="container">
            <table border="1" style="width: 100%; border-collapse: collapse;">
                <tr id="encabezado">
                    <th>Placa del Vehículo</th>
                    <th>Ubicación (Parqueo)</th> <th>Hora de Entrada</th>
                    <th>Estado Actual</th>
                </tr>
                <c:forEach var="a" items="${activeAssignments}">
                    <tr>
                        <td><strong><c:out value="${a.plateVehicle}"/></strong></td>
                        <td><c:out value="${a.lotName}"/></td>
                        <td><c:out value="${a.entryTime}"/></td>
                        <td>
                            <span style="background-color: #27ae60; color: white; padding: 3px 8px; border-radius: 4px; font-size: 0.8rem;">
                                PARQUEADO
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/assignments?action=release&id=${a.id}" 
                               class="cancel" 
                               style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;"
                               onclick="return confirm('¿Confirmar salida del vehículo ${a.plateVehicle}?')">
                                Registrar Salida
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <br>
            <a href="main_menu.html" class="cancel">Volver al Menú</a>
        </div>
    </body>
</html>