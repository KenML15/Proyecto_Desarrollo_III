<%-- 
    Document   : insert_vehicle
    Created on : 11 abr 2026, 12:26:28 a. m.
    Author     : pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ingreso de vehículos</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>

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

                <label>Tipo de Vehículo (ID numérico)</label>
                <input type="number" name="typeId" value="1" required>

                <div class="buttons">
                    <input type="submit" value="Guardar vehiculo" class="save">
                    <input type="reset" value="Cancelar" class="cancel">                
                </div>
            </form>
        </div>

        <a href="main_menu.html" id="boton-volver">Volver al inicio<a/>

    </body>
</html>
