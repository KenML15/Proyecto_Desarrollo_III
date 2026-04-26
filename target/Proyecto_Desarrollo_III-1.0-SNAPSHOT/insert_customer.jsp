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

                <label>Cédula</label>
                <input type="text" name="cedula" placeholder="Ej: 1-2345-6789">

                <label>Teléfono</label>
                <input type="text" name="telefono" placeholder="Ej: 8888-8888">

                <label>Correo Electrónico</label>
                <input type="text" name="correo" placeholder="Ej: cliente@correo.com">

                <div class="checkbox-row">
                    <input type="checkbox" name="discapacity" id="discapacity">
                    <label for="discapacity" class="checkbox-label">
                        ¿El cliente presenta alguna discapacidad?
                    </label>
                </div>

                <div class="buttons">
                    <button type="submit" class="save">Guardar cliente</button>
                    <button type="reset" class="cancel">Limpiar</button>
                </div>
            </form>
        </div>

        <div class="contenedor-centrado">
            <button type="button" onclick="location.href = 'main_menu.html'" class="cancel">
                Volver al inicio
            </button>
        </div>
    </body>
</html>
