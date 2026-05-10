<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Parqueos</title>
    </head>
    <body>

        <div id="titulo">
            <h2>Gestión de Locales de Parqueo</h2>
        </div>

        <%-- Buscador en tiempo real (patrón Lab02) --%>
        <div class="container">
            <label for="search">Buscar parqueo:</label>
            <input type="text" id="search" class="input" placeholder="Nombre del parqueo...">
        </div>

        <div class="container container--wide">
            <table border="1" id="parkingTable">
                <thead>
                    <tr id="encabezado">
                        <th>ID</th>
                        <th>Nombre del Local</th>
                        <th>Capacidad (Espacios)</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${listaParqueos}">
                        <tr>
                            <td><c:out value="${p.id}" /></td>
                            <td><c:out value="${p.name}" /></td>
                            <td><c:out value="${p.numberOfSpaces}" /></td>
                            <td>
                                <a href="parkingLot?action=edit&id=${p.id}" class="save btn-table">Editar</a>
                                <%-- Modal JS en lugar de confirm() nativo --%>
                                <button class="cancel btn-table"
                                        onclick="confirmarEliminar('parkingLot?action=delete&id=${p.id}', '${p.name}')">
                                    Eliminar
                                </button>
                                <a href="assignments?action=list&lotId=${p.id}" class="save btn-table btn-table--info">
                                    Ver Ocupación
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav">
            <a href="menu_admin.jsp" class="cancel">Volver al menú</a>
        </div>

        <%-- Modal de confirmación de eliminación --%>
        <div id="deleteBox" class="confirm-box">
            <div class="confirm-content">
                <p>¿Eliminar el parqueo <strong><span id="deleteNombre"></span></strong>?</p>
                <div class="confirm-buttons">
                    <button class="btn yes" onclick="deleteYes()">Sí, eliminar</button>
                    <button class="btn no"  onclick="deleteNo()">Cancelar</button>
                </div>
            </div>
        </div>

        <script src="js/ParkingLot.js"></script>
    </body>
</html>
