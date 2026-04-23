<%-- Agrega la librería JSTL al inicio --%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Ingreso de vehículos</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>
        <div id="titulo">
            <h2>Ingreso de Vehículos</h2>
        </div>

        <div style="background: yellow; padding: 10px; color: black;">
            Debug: 
            <c:choose>
                <c:when test="${empty customers}">
                    LA LISTA ESTÁ VACÍA O ES NULA.
                </c:when>
                <c:otherwise>
                    HAY CLIENTES CARGADOS. Cantidad: ${customers.size()}
                </c:otherwise>
            </c:choose>
        </div>

        <div class="container">
            <h2>Formulario de ingreso</h2>
            <form action="vehicles" method="post">
                <label>Plate (Placa)</label>
                <input type="text" name="plate" required>

                <label>Color</label>
                <input type="text" name="color">

                <label>Brand (Marca)</label>
                <input type="text" name="brand">

                <label>Model (Modelo)</label>
                <input type="text" name="model">

                <label>Tipo de Vehículo</label>
                <select name="typeId">
                    <option value="1">Automóvil</option>
                    <option value="2">Motocicleta</option>
                    <option value="3">Camión</option>
                </select>

                <label>Asignar Dueño (Cliente)</label>
                <select name="idCustomer">
                    <option value="0">-- Seleccione un cliente --</option>
                    <c:forEach items="${customers}" var="c">
                        <option value="${c.id}">${c.name}</option>
                    </c:forEach>
                </select>

                <div class="buttons">
                    <input type="submit" value="Guardar vehiculo" class="save">
                    <input type="reset" value="Cancelar" class="cancel">                
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