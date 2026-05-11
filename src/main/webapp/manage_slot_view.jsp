<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configuración Manual de Espacios</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body data-menu="menu_admin.jsp">

        <div id="titulo">
            <h2>Configuración Manual de Espacios</h2>
        </div>

        <div class="container container--medium">
            <h2>Parqueo #${lotId}</h2>
            <form action="assignments?action=updateSlots" method="POST">
                <input type="hidden" name="idParkingLot" value="${lotId}">

                <table border="1" id="slotsTable">
                    <thead>
                        <tr id="encabezado">
                            <th># Espacio</th>
                            <th>¿Es Discapacidad?</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="s" items="${spaces}">
                            <tr>
                                <td><strong>Espacio #${s.number}</strong></td>
                                <td class="td-center">
                                    <input type="checkbox" name="slot_${s.number}" ${s.disability ? 'checked' : ''}>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="buttons" style="margin-top: 20px;">
                    <button type="submit" class="save">Guardar Cambios</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <div class="footer-nav footer-nav--lg">
            <a href="assignments?action=dashboard" class="cancel">← Volver al Dashboard</a>
        </div>

        <!-- Modal de confirmación de cancelación (patrón Lab02) -->
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                
                <p>¿Desea cancelar los cambios en los espacios?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes" onclick="confirmYes()">Sí</button>
                    <button class="btn-modal no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/ParkingLot.js"></script>
    </body>
</html>
