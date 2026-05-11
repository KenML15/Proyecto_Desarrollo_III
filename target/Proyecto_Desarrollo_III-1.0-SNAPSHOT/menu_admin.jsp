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
            <% session.removeAttribute("accessError");%>
        </c:if>

        <div class="menu-container">

            <!-- SECCIÓN: Registros (compartida con clerk) -->
            <div class="menu-section">
                <h3> Registros</h3>
                <div class="grid-menu">
                    <a href="insert_customer.jsp" class="menu-card">
                        <span class="label">Insertar Cliente</span>
                    </a>
                    <a href="vehicles?action=add" class="menu-card">
                        <span class="label">Insertar Vehículo</span>
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

            <!-- SECCIÓN: Administración (solo admin) -->
            <div class="menu-section menu-section-admin">
                <h3> Administración (Solo Admin)</h3>
                <div class="grid-menu">
                    <a href="insert_parkinglot.jsp" class="menu-card menu-card-admin">
                        <span class="label">Crear Parqueo</span>
                    </a>
                    <a href="parkingLot" class="menu-card menu-card-admin">
                        <span class="label">Gestionar Parqueos</span>
                    </a>
                    <!-- nuevo -->
                    <a href="vehicleTypes" class="menu-card menu-card-admin">
                        <span class="label">Tipos de Vehículo y Tarifas</span>
                    </a>
                    <a href="show_all_parkingslots" class="menu-card menu-card-admin">
                        <span class="label">Ver Todos los Espacios</span>
                    </a>
                    <a href="manage_slots" class="menu-card menu-card-admin">
                        <span class="label">Administrar Slots</span>
                    </a>
                    <!-- nuevo -->
                    <a href="tickets?action=list" class="menu-card menu-card-admin">
                        <span class="label">Historial de Tiquetes</span>
                    </a>
                    <a href="reports_menu.jsp" class="menu-card menu-card-admin">
                        <span class="label">&#x1F4CA; Reportes PDF</span>
                    </a>
                </div>
            </div>

        </div>

        <div class="logout-container">
            <a href="logout" class="btn-logout">Cerrar Sesión</a>
        </div>
    </body>
</html>
