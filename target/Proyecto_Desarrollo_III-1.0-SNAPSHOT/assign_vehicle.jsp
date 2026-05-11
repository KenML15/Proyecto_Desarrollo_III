<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Asignar Vehículo | Registro Inteligente</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body data-menu="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}">

        <div class="container">
            <div id="titulo">
                <h2>Registrar Ingreso de Vehículo</h2>
                <p style="text-align: center; color: #718096; font-size: 0.9em;">
                    El sistema asignará automáticamente el espacio disponible.
                </p>
            </div>

            <form action="assignments" method="POST" class="form" id="parkingForm">
                <label>SELECCIONAR VEHÍCULO (PLACA):</label>
                <select name="plateVehicle" id="plateVehicle" required>
                    <option value="">-- Seleccione un vehículo --</option>
                    <c:forEach var="v" items="${vehicles}">
                        <option value="${v.plate}">${v.plate} - ${v.brand} ${v.model}</option>
                    </c:forEach>
                </select>

                <label>SELECCIONAR PARQUEO / LOTE:</label>
                <select name="idParkingLot" id="idParkingLot" required>
                    <option value="">-- Seleccione el área de destino --</option>
                    <c:forEach var="p" items="${parkingLots}">
                        <option value="${p.id}">${p.name}</option>
                    </c:forEach>
                </select>

                <div class="botones" style="margin-top: 20px;">
                    <button type="submit" class="save">Registrar Ingreso</button>
                    <button type="button" class="cancel" id="btnCancelar">Cancelar</button>
                </div>
            </form>
        </div>

        <div id="confirmBox" class="confirm-box" style="display: none;">
            <div class="confirm-content">
                <p>¿Desea cancelar el registro de ingreso?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" id="confirmYes">Sí</button>
                    <button class="btn no"  id="confirmNo">No</button>
                </div>
            </div>
        </div>

        <script src="js/Assignment.js"></script>
    </body>
</html>