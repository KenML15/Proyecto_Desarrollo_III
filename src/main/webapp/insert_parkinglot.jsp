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

            <form action="parkingLot"  method="post">

                <label>Id</label>
                <input type="text" name="id">

                <label>Name</label>
                <input type="text" name="name">

                <label>Number Of Spaces</label>
                <input type="text" name="num_spaces">

                <div class="buttons">
                    <input type="submit" value="Guardar parqueo" class="save">
                    <input type="reset" value="Cancelar" class="cancel">
                </div>

            </form>
        </div>
        <a href="main_menu.html" id="boton-volver">Volver al inicio<a/>
    </body>
</html>
