<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Ingreso de Vehículos</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body data-menu="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}">

        <div id="titulo">
            <h2>Ingreso de Vehículos</h2>
        </div>

        <div class="container">
            <h2>Formulario de ingreso</h2>
            <form action="vehicles" method="post">

                <label>Plate (Placa)</label>
                <input type="text" name="plate" required>

                <label>Color</label>
                <input type="text" name="color">

                <label>Brand (Marca)</label>
                <input type="text" name="brand">

                <label>Model (Modelo)</label>
                <input type="text" name="model">

                <label>Tipo de Vehículo</label>
                <select name="typeId">
                    <option value="1">Automóvil</option>
                    <option value="2">Motocicleta</option>
                    <option value="3">Camión</option>
                </select>


                <div class="buttons">
                    <input type="submit" value="Guardar vehículo" class="save">
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <%-- Modal de confirmación de cancelación (patrón Lab02) --%>
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <p>¿Desea cancelar el registro del vehículo?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" onclick="confirmYes()">Sí</button>
                    <button class="btn no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/Vehicle.js"></script>
    </body>
</html>
