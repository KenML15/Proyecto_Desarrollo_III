<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configuración de Espacios</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body data-menu="menu_admin.jsp">

        <div id="titulo">
            <h2>Configurar Espacios — Parqueo #${lotId}</h2>
        </div>

        <div class="container">
            <h2>Agregar nuevo espacio</h2>
            <form action="assignments?action=saveSlot" method="POST">
                <input type="hidden" name="idParkingLot" value="${lotId}">

                <label>Número de Espacio</label>
                <input type="number" name="slotNumber" min="1" required placeholder="Ej: 1">

                <div class="checkbox-row">
                    <input type="checkbox" name="isDisability" id="isDisability">
                    <label for="isDisability" class="checkbox-label">¿Es para discapacidad?</label>
                </div>

                <div class="buttons">
                    <button type="submit" class="save">Guardar Espacio</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <div class="container container--medium" style="margin-top: 24px;">
            <h2>Espacios actuales registrados</h2>
            <table border="1" id="slotsTable">
                <thead>
                    <tr id="encabezado">
                        <th># Espacio</th>
                        <th>Tipo de Espacio</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${currentSlots}">
                        <tr>
                            <td><strong>Espacio ${s.number}</strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${s.disability}">
                                        <span class="badge-disability">Discapacidad</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-standard">Estándar</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty currentSlots}">
                        <tr>
                            <td colspan="2" class="td-center text-unassigned">
                                No hay espacios configurados aún.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div class="footer-nav footer-nav--lg">
            <a href="assignments?action=dashboard" class="cancel">← Volver al Dashboard</a>
        </div>

        <!-- Modal de confirmación de cancelación (patrón Lab02) -->
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                
                <p>¿Desea cancelar la configuración del espacio?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes" onclick="confirmYes()">Sí</button>
                    <button class="btn-modal no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/ParkingLot.js"></script>
    </body>
</html>
