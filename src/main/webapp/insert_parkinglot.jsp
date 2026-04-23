<%-- 
    Document   : insert_parckinglot
    Created on : 11 abr 2026, 5:13:44 p. m.
    Author     : pablo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insertar parqueo</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>

    <body>

        <div id="titulo">

            <h2>Creacion de parqueos</h2>

        </div>

        <div class="container">
            <h2>Datos a ingresar</h2>
            <form action="parkingLot" method="post">
                <label>Nombre del Parqueo</label>
                <input type="text" name="name" required placeholder="Ej: Parqueo Central">

                <label>Cantidad de Espacios</label>
                <input type="number" name="num_spaces" required placeholder="Ej: 50">

                <div class="buttons">
                    <input type="submit" value="Guardar parqueo" class="save">
                    <input type="reset" value="Limpiar" class="cancel">
                </div>
            </form>
        </div>

    </form>
</div>
<div class="contenedor-centrado" style="text-align: center; margin-top: 20px;">
    <button type="button" onclick="location.href = 'main_menu.html'" class="cancel" style="padding: 10px 20px;">
        Volver al inicio
    </button>
</div>
</body>
</html>
