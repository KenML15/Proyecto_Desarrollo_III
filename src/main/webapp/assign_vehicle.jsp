<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Asignar Vehículo a Parqueo</title>
    </head>
    <body data-menu="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}">

        <div class="container">
            <div id="titulo">
                <h2>Registrar Ingreso de Vehículo</h2>
            </div>

            <form action="assignments" method="POST" class="form">

                <label>SELECCIONAR VEHÍCULO (PLACA):</label>
                <select name="plateVehicle" required>
                    <option value="">-- Seleccione un vehículo --</option>
                    <c:forEach var="v" items="${vehicles}">
                        <option value="${v.plate}">${v.plate} - ${v.brand} ${v.model}</option>
                    </c:forEach>
                </select>

                <label>SELECCIONAR PARQUEO / LOTE:</label>
                <select name="idParkingLot" required>
                    <option value="">-- Seleccione el área de destino --</option>
                    <c:forEach var="p" items="${parkingLots}">
                        <option value="${p.id}">${p.name} (Capacidad: ${p.numberOfSpaces})</option>
                    </c:forEach>
                </select>

                <label>NÚMERO DE ESPACIO (SLOT):</label>
                <input type="number" name="assignedSlot" placeholder="Ej: 1" required min="1">

                <div class="botones">
                    <button type="submit" class="save">Registrar Ingreso</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <%-- Modal de confirmación de cancelación (patrón Lab02) --%>
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <p>¿Desea cancelar el registro de ingreso?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" onclick="confirmYes()">Sí</button>
                    <button class="btn no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/Assignment.js"></script>
    </body>
</html>
