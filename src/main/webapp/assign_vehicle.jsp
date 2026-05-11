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

        <div id="titulo"><h2>Registrar Ingreso de Vehículo</h2></div>

        <div class="container">
            <form action="assignments" method="POST" class="form">
                <label>Seleccionar Vehículo (Placa)</label>
                <select name="plateVehicle" required>
                    <option value="">-- Seleccione un vehículo --</option>
                    <c:forEach var="v" items="${vehicles}">
                        <option value="${v.plate}">${v.plate} - ${v.brand} ${v.model}</option>
                    </c:forEach>
                </select>
                <label>Seleccionar Parqueo / Lote</label>
                <select name="idParkingLot" required>
                    <option value="">-- Seleccione el área de destino --</option>
                    <c:forEach var="p" items="${parkingLots}">
                        <option value="${p.id}">${p.name} (Capacidad: ${p.numberOfSpaces})</option>
                    </c:forEach>
                </select>
                <label>Número de Espacio (Slot)</label>
                <input type="number" name="assignedSlot" placeholder="Ej: 1" required min="1">
                <div class="buttons">
                    <button type="submit" class="save">Registrar Ingreso</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <!-- Modal de confirmación (patrón Lab02 + estilos del proyecto) -->
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <span class="modal-icon">🚪</span>
                <p>¿Desea cancelar el registro de ingreso?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes" onclick="confirmYes()">Sí</button>
                    <button class="btn-modal no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/Assignment.js"></script>
    </body>
</html>
