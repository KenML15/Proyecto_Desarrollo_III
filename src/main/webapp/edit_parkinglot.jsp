<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Parqueo</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>

        <div id="titulo">
            <h2>Modificar Datos del Parqueo</h2>
        </div>

        <div class="container">
            <form action="parkingLot" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${parqueo.id}">

                <label>Nombre del Parqueo</label>
                <input type="text" name="name" value="${parqueo.name}">

                <label>Cantidad de Espacios</label>
                <input type="number" name="num_spaces" value="${parqueo.numberOfSpaces}">

                <div class="buttons">
                    <input type="submit" value="Actualizar" class="save">
                    <a href="parkingLot" class="cancel" style="text-decoration:none; padding:10px;">Cancelar</a>
                </div>
            </form>
        </div>

        <div style="text-align: center; margin-top: 20px;">
            <a href="main_menu.html" id="boton-volver">Volver al inicio</a>
        </div>

    </body>
</html>