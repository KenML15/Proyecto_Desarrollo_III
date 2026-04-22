<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Ingreso de Clientes</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>
        <div id="titulo">
            <h2>Gestión de Clientes</h2>
        </div>

        <div class="container">
            <h2>Formulario de registro</h2>

            <form action="customers" method="post">
                <label>Nombre Completo</label>
                <input type="text" name="name" required placeholder="Ej: Kenneth Miranda">

                <div style="margin: 20px 0; display: flex; align-items: center; gap: 10px;">
                    <input type="checkbox" name="discapacity" id="discapacity" style="width: auto; margin: 0;">
                    <label for="discapacity" style="margin: 0; font-weight: normal;">
                        ¿El cliente presenta alguna discapacidad?
                    </label>
                </div>

                <div class="buttons">
                    <button type="submit" class="save">Guardar cliente</button>
                    <button type="reset" class="cancel">Limpiar</button>
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