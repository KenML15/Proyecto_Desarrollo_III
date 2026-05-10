<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Insertar Parqueo</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body data-menu="menu_admin.jsp">

        <div id="titulo">
            <h2>Creación de Parqueos</h2>
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
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <%-- Modal de confirmación de cancelación (patrón Lab02) --%>
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <p>¿Desea cancelar el registro del parqueo?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" onclick="confirmYes()">Sí</button>
                    <button class="btn no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/ParkingLot.js"></script>
    </body>
</html>
