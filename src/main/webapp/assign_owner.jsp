<%-- 
    Document   : assign_owner
    Created on : 10 may 2026, 7:01:35 p.m.
    Author     : Kenneth
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Asignar Dueño a Vehículo</title>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body data-menu="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}">

        <div class="container">
            <div id="titulo">
                <h2>Gestión de Propietarios</h2>
            </div>

            <form action="vehicles" method="POST" class="form">
                <input type="hidden" name="action" value="assignOwner">

                <label>SELECCIONAR VEHÍCULO:</label>
                <select name="plate" required>
                    <option value="">-- Seleccione placa --</option>
                    <c:forEach var="v" items="${vehicles}">
                        <option value="${v.plate}">${v.plate} (${v.brand} ${v.model})</option>
                    </c:forEach>
                </select>

                <label>SELECCIONAR CLIENTE:</label>
                <select name="idCustomer" required>
                    <option value="">-- Seleccione cliente --</option>
                    <c:forEach var="c" items="${customers}">
                        <option value="${c.id}">${c.name}</option>
                    </c:forEach>
                </select>

                <div class="buttons">
                    <button type="submit" class="save">Vincular Dueño</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <script src="js/Assignment.js"></script>
    </body>
</html>