<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Vehículo</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>
        
        <div id="titulo">
            <h2>Modificar Datos del Vehículo</h2>
        </div>

        <div class="container">
            
            <h2>Formulario de edición</h2>

            <form action="vehicles?action=update" method="post">

                <label>Plate (Placa)</label>
                <input type="text" name="plate" value="${vehicle.plate}" readonly style="background-color: #eee;">

                <label>Color</label>
                <input type="text" name="color" value="${vehicle.color}" required>

                <label>Brand (Marca)</label>
                <input type="text" name="brand" value="${vehicle.brand}" required>

                <label>Model (Modelo)</label>
                <input type="text" name="model" value="${vehicle.model}" required>

                <label>Owner ID (ID Propietario)</label>
                <input type="text" name="id" value="${vehicle.id}" required>

                <label>Description</label>
                <input type="text" name="description" value="${vehicle.description}">

                <label>Number of Tires</label>
                <input type="number" name="num_tires" value="${vehicle.numTires}" required>

                <label>Fee (Tarifa)</label>
                <input type="number" step="0.01" name="fee" value="${vehicle.fee}" required>

                <div class="buttons">
                    <input type="submit" value="Actualizar Vehículo" class="save">
                    <a href="vehicles" class="cancel" style="text-decoration: none; display: inline-block; text-align: center; line-height: normal;">Cancelar</a>
                </div>
            </form>
        </div>
        
        <div style="text-align: center; margin-top: 20px;">
            <a href="main_menu.html" id="boton-volver">Volver al inicio</a>
        </div>

    </body>
</html>