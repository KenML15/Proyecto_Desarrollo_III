<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Clientes</title>
    </head>
    <body>
        <div id="titulo"><h2>Gestión de Clientes</h2></div>

        <!-- Buscador en tiempo real (patrón Lab02) -->
        <div class="search-wrapper">
            <label for="search">Buscar cliente</label>
            <input type="text" id="search" class="input-search" placeholder="Nombre, cédula, correo...">
        </div>

        <div class="container container--wide">
            <table border="1" id="customerTable">
                <thead>
                    <tr id="encabezado">
                        <th>ID</th><th>Nombre</th><th>Cédula</th>
                        <th>Teléfono</th><th>Correo</th><th>Discapacidad</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${customers}" var="c">
                        <tr>
                            <td><c:out value="${c.id}"/></td>
                            <td><c:out value="${c.name}"/></td>
                            <td><c:out value="${c.cedula}"/></td>
                            <td><c:out value="${c.telefono}"/></td>
                            <td><c:out value="${c.correo}"/></td>
                            <td><c:out value="${c.disabilityPresented ? 'Sí' : 'No'}"/></td>
                            <td>
                                <a href="customers?action=edit&id=${c.id}" class="save btn-table">Editar</a>
                                <!-- Modal JS en lugar de confirm() nativo -->
                                <button class="cancel btn-table"
                                        onclick="confirmarEliminar('customers?action=delete&id=${c.id}','${c.name}')">
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
                <span class="modal-icon">🗑️</span>
                <p>¿Eliminar al cliente <strong><span id="deleteNombre"></span></strong>?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes-danger" onclick="deleteYes()">Sí, eliminar</button>
                    <button class="btn-modal no"         onclick="deleteNo()">Cancelar</button>
                </div>
            </div>
        </div>

        <!-- Script al final del body, igual que en el Lab02 -->
        <script src="js/Customer.js"></script>
    </body>
</html>
