<%-- 
    Document   : manage_slot_view
    Created on : 9 may 2026, 6:43:12 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <div class="manage-container">
            <h2 style="color: white; text-align: center;">Configuración de Espacios Manual</h2>
            <form action="assignments?action=updateSlots" method="POST">
                <input type="hidden" name="idParkingLot" value="${lotId}">
                <table class="manage-table">
                    <tr>
                        <th># Espacio</th>
                        <th>¿Es Discapacidad?</th>
                    </tr>
                    <c:forEach var="s" items="${spaces}">
                        <tr>
                            <td><strong>Espacio #${s.number}</strong></td>
                            <td>
                                <input type="checkbox" name="slot_${s.number}" ${s.disability ? 'checked' : ''}>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <button type="submit" class="btn-save-slots">Guardar Cambios</button>
            </form>
        </div>
    </body>
</html>
