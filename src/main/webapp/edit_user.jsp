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
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar Usuario</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body data-menu="menu_admin.jsp">

        <div id="titulo"><h2>Modificar Datos del Usuario</h2></div>

        <div class="container">

            <c:if test="${not empty error}">
                <p style="color: #ff4d4d; text-align: center; margin-bottom: 16px;">
                    ⚠ ${error}
                </p>
            </c:if>

            <form action="users" method="post">
                <input type="hidden" name="action" value="update">

                <label>ID</label>
                <input type="text" name="id" value="${user.id}" readonly>

                <label>Nombre de usuario</label>
                <input type="text" name="username" value="${user.username}" required>

                <label>Nueva contraseña <span style="font-size:0.8rem; color:#94a3b8;">(dejar en blanco para no cambiar)</span></label>
                <input type="password" name="password" placeholder="Nueva contraseña...">

                <label>Rol</label>
                <select name="role" required>
                    <option value="admin"  ${user.role == 'admin'  ? 'selected' : ''}>Administrador</option>
                    <option value="clerk"  ${user.role == 'clerk'  ? 'selected' : ''}>Operador (Dependiente)</option>
                </select>

                <div class="checkbox-row">
                    <input type="checkbox" name="active" id="active" ${user.active == 1 ? 'checked' : ''}>
                    <label for="active" class="checkbox-label">¿Usuario activo?</label>
                </div>

                <div class="buttons">
                    <input type="submit" value="Actualizar Cambios" class="save">
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                <span class="modal-icon">✏️</span>
                <p>¿Desea cancelar la edición del usuario?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes" onclick="confirmYes()">Sí</button>
                    <button class="btn-modal no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/User.js"></script>
    </body>
</html>
