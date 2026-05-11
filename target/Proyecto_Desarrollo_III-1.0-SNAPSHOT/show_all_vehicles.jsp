<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Vehículos</title>
    </head>
    <body>
        <div id="titulo"><h2>Vehículos Registrados</h2></div>

        <!-- Buscador en tiempo real (patrón Lab02) -->
        <div class="search-wrapper">
            <label for="search">Buscar vehículo</label>
            <input type="text" id="search" class="input-search" placeholder="Placa, marca, modelo, color...">
        </div>

        <div class="container container--medium">
            <table border="1" id="vehicleTable">
                <thead>
                    <tr id="encabezado">
                        <th>Placa</th><th>Marca</th><th>Modelo</th>
                        <th>Color</th><th>Dueño</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="v" items="${vehicles}">
                        <tr>
                            <td><c:out value="${v.plate}"/></td>
                            <td><c:out value="${v.brand}"/></td>
                            <td><c:out value="${v.model}"/></td>
                            <td><c:out value="${v.color}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty v.ownerName}"><c:out value="${v.ownerName}"/></c:when>
                                    <c:otherwise><span class="text-unassigned">Sin asignar</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="vehicles?action=edit&plate=${v.plate}" class="save btn-table">Editar</a>
                                <button class="cancel btn-table"
                                        onclick="confirmarEliminar('vehicles?action=delete&plate=${v.plate}','${v.plate}')">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav">
            <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                ← Volver al menú
            </a>
        </div>

        <!-- Modal de eliminación -->
        <div id="deleteBox" class="confirm-box">
            <div class="confirm-content">
                
                <p>¿Eliminar el vehículo <strong><span id="deleteNombre"></span></strong>?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes-danger" onclick="deleteYes()">Sí, eliminar</button>
                    <button class="btn-modal no"         onclick="deleteNo()">Cancelar</button>
                </div>
            </div>
        </div>

        <script src="js/Vehicle.js"></script>
    </body>
</html>
