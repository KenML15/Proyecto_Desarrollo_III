<%-- 
    Document   : insert_customer
    Created on : 10 abr 2026, 11:28:58 p. m.
    Author     : pablo
--%>

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

            <h2>Ingreso de Clientes</h2>

        </div>

        <div class="container">

            <h2>Formulario de ingreso</h2>

            <form action="customers" method="post">

                <label>Id</label>
                <input type="text" name="id">

                <label>Name</label>
                <input type="text" name="name">

                <label>Disability</label>
                <input type="text" name="discapacity">

                <div class="buttons">
                    <input type="submit" value="Guardar cliente" class="save">
                    <input type="reset" value="Cancelar" class="cancel">
                </div>
            </form>
        </div>
        <div class="contenedor-centrado">
            <button type="button" id="boton_volver" onclick="history.back()">
                Volver atrás
            </button>
        </div>
    </body>
</html>
