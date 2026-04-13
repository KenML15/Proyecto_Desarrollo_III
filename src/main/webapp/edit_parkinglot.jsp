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
            <form action="parkingLot?action=update" method="post">

                <label>Identificador (ID)</label>
                <input type="text" name="id" value="${parkingLot.id}" readonly style="background-color: #f0f0f0;">

                <label>Nombre del Parqueo</label>
                <input type="text" name="name" value="${parkingLot.name}" required>

                <label>Número de Espacios</label>
                <input type="number" name="num_spaces" value="${parkingLot.numSpaces}" required>

                <div style="margin: 20px 0; display: flex; align-items: center; gap: 10px;">
                    <input type="checkbox" name="has_disability_spaces" id="disability_spaces" 
                           ${parkingLot.hasDisabilitySpaces ? 'checked' : ''} style="width: auto; margin: 0;">
                    <label for="disability_spaces" style="margin: 0; font-weight: normal;">
                        ¿Cuenta con espacios para personas con discapacidad?
                    </label>
                </div>

                <div class="buttons">
                    <input type="submit" value="Actualizar Parqueo" class="save">
                    <a href="parkingLot" class="cancel" style="text-decoration: none; display: inline-block; text-align: center; line-height: normal;">Cancelar</a>
                </div>
            </form>
        </div>
        
        <div style="text-align: center; margin-top: 20px;">
            <a href="main_menu.html" id="boton-volver">Volver al inicio</a>
        </div>

    </body>
</html>