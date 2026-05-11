<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Protección de sesión: si no hay sesión o no es admin, redirigir --%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("menu_clerk.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Menú Administrador</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>
        <div class="header-container">
            <img src="IMG/logo.png" alt="Logo" class="logo">
            <div id="titulo">
                <h2>Panel de Administrador</h2>
                <p class="role-badge role-admin">&#x1F6E1; Administrador: ${sessionScope.username}</p>
            </div>
        </div>

        <%-- Mensaje de error de acceso (viene de RoleFilter) --%>
        <c:if test="${not empty sessionScope.accessError}">
            <p class="error-msg">${sessionScope.accessError}</p>
            <% session.removeAttribute("accessError"); %>
        </c:if>

        <div class="menu-container">

            <div class="menu-section">
                <h3> Registros</h3>
                <div class="grid-menu">
                    <a href="insert_customer.jsp" class="menu-card">
                        <span class="label">Insertar Cliente</span>
                    </a>
                    <a href="vehicles?action=add" class="menu-card">
                        <span class="label">Insertar Vehículo</span>
                    </a>
                    
                    <a href="vehicles?action=prepareAssign" class="menu-card">
                        <span class="label">Vincular Dueño a Vehículo</span>
                    </a>

                    <a href="assignments?action=prepare" class="menu-card">
                        <span class="label">Ingresar Vehículo a Parqueo</span>
                    </a>
                    <a href="customers" class="menu-card">
                        <span class="label">Gestionar Clientes</span>
                    </a>
                    <a href="vehicles?action=list" class="menu-card">
                        <span class="label">Gestionar Vehículos</span>
                    </a>
                    <a href="assignments?action=dashboard" class="menu-card">
                        <span class="label">Panel de Espacios</span>
                    </a>
                </div>
            </div>

            <div class="menu-section menu-section-admin">
                <h3> Administración (Solo Admin)</h3>
                <div class="grid-menu">
                    <a href="insert_parkinglot.jsp" class="menu-card menu-card-admin">
                        <span class="label">Crear Parqueo</span>
                    </a>
                    <a href="parkingLot" class="menu-card menu-card-admin">
                        <span class="label">Gestionar Parqueos</span>
                    </a>
                    <a href="parking_board_view.jsp" class="menu-card menu-card-admin">
                        <span class="label">Ver Todos los Espacios</span>
                    </a>
                    <a href="manage_slots.jsp" class="menu-card menu-card-admin">
                        <span class="label">Administrar Slots</span>
                    </a>
                    <a href="users?action=add" class="menu-card menu-card-admin">
                        <span class="label">Crear Usuario</span>
                    </a>
                    <a href="users" class="menu-card menu-card-admin">
                        <span class="label">Gestionar Usuarios</span>
                    </a>
                </div>
            </div>

        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get('msg') === 'assigned') {
                Swal.fire('¡Éxito!', 'Dueño vinculado correctamente', 'success');
            }
            if (urlParams.get('msg') === 'created') {
                Swal.fire('¡Éxito!', 'Vehículo registrado', 'success');
            }
        </script>

        <div class="logout-container">
            <a href="logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </body>
</html>
