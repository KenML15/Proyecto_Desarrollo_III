<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Cliente</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>
        
        <div id="titulo">
            <h2>Modificar Datos del Cliente</h2>
        </div>

        <div class="container">
            <form action="customers?action=update" method="post">

                <label>Identificación (ID)</label>
                <input type="text" name="id" value="${customer.id}" readonly style="background-color: #eee;">

                <label>Nombre Completo</label>
                <input type="text" name="name" value="${customer.name}" required>

                <div style="margin: 15px 0;">
                    <label>
                        <input type="checkbox" name="discapacity" ${customer.disabilityPresented ? 'checked' : ''}>
                        ¿Presenta alguna discapacidad?
                    </label>
                </div>

                <div class="buttons">
                    <input type="submit" value="Actualizar Cambios" class="save">
                    <a href="customers" class="cancel" style="text-decoration: none; display: inline-block; text-align: center; line-height: normal;">Cancelar</a>
                </div>
            </form>
        </div>
        
        <div style="text-align: center; margin-top: 20px;">
            <a href="main_menu.html" id="boton-volver">Volver al inicio</a>
        </div>

    </body>
</html>