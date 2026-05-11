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
        <title>Crear Usuario</title>
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body data-menu="menu_admin.jsp">

        <div id="titulo"><h2>Gestión de Usuarios</h2></div>

        <div class="container">
            <h2>Formulario de registro</h2>

            <c:if test="${not empty error}">
                <p style="color: #ff4d4d; text-align: center; margin-bottom: 16px;">
                    ${error}
                </p>
            </c:if>

            <form action="users" method="post">
                <input type="hidden" name="action" value="insert">

                <label>Nombre de usuario</label>
                <input type="text" name="username" required placeholder="Ej: jperez"
                       value="${param.username}">

                <label>Contraseña</label>
                <input type="password" name="password" required placeholder="Mínimo 6 caracteres">

                <label>Rol</label>
                <select name="role" required>
                    <option value="">-- Seleccione un rol --</option>
                    <option value="admin">Administrador</option>
                    <option value="clerk">Operador (Dependiente)</option>
                </select>

                <div class="checkbox-row">
                    <input type="checkbox" name="active" id="active" checked>
                    <label for="active" class="checkbox-label">¿Usuario activo?</label>
                </div>

                <div class="buttons">
                    <button type="submit" class="save">Guardar usuario</button>
                    <button type="button" class="cancel" onclick="cancelar()">Cancelar</button>
                </div>
            </form>
        </div>

        <div id="confirmBox" class="confirm-box">
            <div class="confirm-content">
                
                <p>¿Desea cancelar el registro del usuario?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes" onclick="confirmYes()">Sí</button>
                    <button class="btn-modal no"  onclick="confirmNo()">No</button>
                </div>
            </div>
        </div>

        <script src="js/User.js"></script>
    </body>
</html>
