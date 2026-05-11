<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (session.getAttribute("username") == null || !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Usuarios</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>

        <div id="titulo"><h2>Gestión de Usuarios</h2></div>

        <div class="search-wrapper">
            <label for="search">Buscar usuario</label>
            <input type="text" id="search" class="input-search" placeholder="Nombre, rol...">
        </div>

        <div class="container container--wide">
            <table border="1" id="userTable">
                <thead>
                    <tr id="encabezado">
                        <th>ID</th>
                        <th>Usuario</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="u">
                        <tr>
                            <td><c:out value="${u.id}"/></td>
                            <td><c:out value="${u.username}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.role == 'admin'}">
                                        <span class="role-badge role-admin">&#x1F6E1; Administrador</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="role-badge role-clerk">&#x1F464; Operador</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.active == 1}">
                                        <span style="color:#4ecca3; font-weight:600;">● Activo</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color:#ff4d4d; font-weight:600;">● Inactivo</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="users?action=edit&id=${u.id}" class="save btn-table">Editar</a>
                                <button class="cancel btn-table"
                                        onclick="confirmarEliminar('users?action=delete&id=${u.id}', '${u.username}')">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav">
            <a href="users?action=add" class="save">+ Nuevo usuario</a>
            <a href="menu_admin.jsp" class="cancel">← Volver al menú</a>
        </div>

        <!-- Modal de confirmación de eliminación -->
        <div id="deleteBox" class="confirm-box">
            <div class="confirm-content">
                <span class="modal-icon">🗑️</span>
                <p>¿Eliminar al usuario <strong><span id="deleteNombre"></span></strong>?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes-danger" onclick="deleteYes()">Sí, eliminar</button>
                    <button class="btn-modal no"         onclick="deleteNo()">Cancelar</button>
                </div>
            </div>
        </div>

        <script src="js/User.js"></script>

        <script>
            // Feedback de acciones exitosas
            const params = new URLSearchParams(window.location.search);
            if (params.get('msg') === 'created') {
                alert('✅ Usuario creado correctamente.');
            } else if (params.get('msg') === 'updated') {
                alert('✅ Usuario actualizado correctamente.');
            } else if (params.get('error') === 'db') {
                alert('❌ Error al guardar. Intente de nuevo.');
            }
        </script>
    </body>
</html>
