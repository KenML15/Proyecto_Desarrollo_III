<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Cliente</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body data-menu="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}">

        <div id="titulo">
            <h2>Modificar Datos del Cliente</h2>
        </div>

        <div class="container">
            <form action="customers?action=update" method="post">

                <label>Identificación (ID)</label>
                <input type="text" name="id" value="${customer.id}" readonly>

                <label>Nombre Completo</label>
                <input type="text" name="name" value="${customer.name}" required>

                <label>Cédula</label>
                <input type="text" name="cedula" value="${customer.cedula}" placeholder="Ej: 1-2345-6789">

                <label>Teléfono</label>
                <input type="text" name="telefono" value="${customer.telefono}" placeholder="Ej: 8888-8888">

                <label>Correo Electrónico</label>
                <input type="text" name="correo" value="${customer.correo}" placeholder="Ej: cliente@correo.com">

                <div class="form-check">
                    <label>
                        <input type="checkbox" name="discapacity" ${customer.disabilityPresented ? 'checked' : ''}>
                        ¿Presenta alguna discapacidad?
                    </label>
                </div>

                <div class="buttons">
                    <input type="submit" value="Actualizar Cambios" class="save">
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <%-- Modal de confirmación de cancelación (patrón Lab02) --%>
        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <p>¿Desea cancelar la edición del cliente?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" onclick="confirmYes()">Sí</button>
                    <button class="btn no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/Customer.js"></script>
    </body>
</html>
