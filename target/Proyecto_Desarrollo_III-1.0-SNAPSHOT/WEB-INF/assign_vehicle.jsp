<%-- 
    Document   : assign_vehicle
    Created on : 22 abr 2026, 2:11:54 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container">
    <h2>Asignar Dueño a Vehículo</h2>
    <form action="vehicles" method="post">
        <input type="hidden" name="action" value="assign">

        <label>Seleccione el Vehículo (Placa)</label>
        <select name="plate">
            <c:forEach var="v" items="${vehicles}">
                <option value="${v.plate}">${v.plate} - ${v.brand} ${v.model}</option>
            </c:forEach>
        </select>

        <label>Seleccione el Cliente (Dueño)</label>
        <select name="idCustomer">
            <c:forEach var="c" items="${customers}">
                <option value="${c.id}">${c.name}</option>
            </c:forEach>
        </select>

        <div class="buttons">
            <button type="submit" class="save">Vincular Cliente</button>
        </div>
    </form>
</div>
    </body>
</html>
