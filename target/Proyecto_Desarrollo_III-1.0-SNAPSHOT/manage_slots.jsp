<%-- 
    Document   : manageslots
    Created on : 9 may 2026, 6:35:42 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configuración de Espacios</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <div class="manage-container">
            <h2>Configurar Espacios - Parqueo #${lotId}</h2>

            <form action="assignments?action=saveSlot" method="POST" class="manage-form">
                <input type="hidden" name="idParkingLot" value="${lotId}">

                <div class="form-group">
                    <label>Número de Espacio:</label>
                    <input type="number" name="slotNumber" min="1" required placeholder="Ej: 1">
                </div>

                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" name="isDisability"> ¿Es para discapacidad?
                    </label>
                </div>

                <button type="submit" class="btn-save-slots">Guardar Espacio</button>
            </form>

            <hr>

            <h3>Espacios actuales registrados</h3>
            <table class="manage-table">
                <thead>
                    <tr>
                        <th># Espacio</th>
                        <th>Tipo de Espacio</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${currentSlots}">
                        <tr>
                            <td><strong>Espacio ${s.number}</strong></td>
                            <td>
                                <span class="${s.disability ? 'status-blue' : 'status-gray'}">
                                    ${s.disability ? "♿ Discapacidad" : "Estándar"}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty currentSlots}">
                        <tr>
                            <td colspan="2" style="text-align: center; color: #a0aec0;">
                                No hay espacios configurados aún.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <div style="margin-top: 20px; text-align: center;">
                <a href="assignments?action=dashboard" class="btn-table" style="text-decoration: none; background: #4a5568;">
                    Volver al Dashboard
                </a>
            </div>
        </div> 
        <div class="footer-nav">
            <a href="menu_admin.jsp" id="boton-volver">Volver al menu</a>
        </div>

    </body>
</html>