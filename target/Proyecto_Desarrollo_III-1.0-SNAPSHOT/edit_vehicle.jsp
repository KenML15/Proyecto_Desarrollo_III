<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Editar Vehículo</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <div id="titulo">
            <h2>Modificar Datos del Vehículo</h2>
        </div>
        <div class="container">
            <form action="vehicles" method="post">
                <input type="hidden" name="action" value="update">

                <label>Placa (No editable)</label>
                <input type="text" name="plate" value="${vehicle.plate}" readonly>

                <label>Color</label>
                <input type="text" name="color" value="${vehicle.color}">

                <label>Marca</label>
                <input type="text" name="brand" value="${vehicle.brand}">

                <label>Modelo</label>
                <input type="text" name="model" value="${vehicle.model}">

                <label>Tipo de Vehículo</label>
                <select name="typeId">
                    <option value="1" ${vehicle.idVehicleType == 1 ? 'selected' : ''}>Automóvil</option>
                    <option value="2" ${vehicle.idVehicleType == 2 ? 'selected' : ''}>Motocicleta</option>
                    <option value="3" ${vehicle.idVehicleType == 3 ? 'selected' : ''}>Camión</option>
                </select>

                <div class="buttons">
                    <input type="submit" value="Actualizar" class="save">
                    <a href="vehicles" class="cancel" style="text-decoration:none; padding:10px;">Cancelar</a>
                </div>
            </form>
        </div>
    </body>
</html>