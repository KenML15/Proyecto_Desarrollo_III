<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    if (session.getAttribute("username") == null) { response.sendRedirect("login.jsp"); return; }
    if ("admin".equals(session.getAttribute("role"))) { response.sendRedirect("menu_admin.jsp"); return; }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Panel de Operaciones | Sistema de Parqueo</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>
    <body>

        <div class="header-container">
            <img src="IMG/logo.png" alt="Logo" class="logo" style="max-height: 80px;">
            <div id="titulo">
                <h1 style="margin: 10px 0;">Panel de Operaciones</h1>
                <p class="role-badge"><strong>Clerk:</strong> ${sessionScope.username}</p>
            </div>
        </div>

        <div class="menu-container" style="max-width: 1100px; margin: 0 auto; padding: 0 20px;">

            <div class="menu-section">
                <h3 class="section-title">Operaciones de Parqueo</h3>
                <div class="grid-menu">
                    <a href="assignments?action=prepare" class="menu-card menu-card-admin">
                        <span class="label">Ingresar Vehículo</span>
                    </a>
                    <a href="tickets?action=list" class="menu-card menu-card-admin">
                        <span class="label">Gestionar Salidas</span>
                    </a>
                    <a href="assignments?action=dashboard" class="menu-card menu-card-admin">
                        <span class="label">Panel de Espacios</span>
                    </a>
                    <a href="assignments?action=list" class="menu-card menu-card-admin">
                        <span class="label">Ocupación Actual</span>
                    </a>
                </div>
            </div>

            <div class="menu-section" style="margin-top: 30px;">
                <h3 class="section-title">Mantenimiento de Datos</h3>
                <div class="grid-menu">
                    <a href="insert_customer.jsp" class="menu-card menu-card-admin">
                        <span class="label">Nuevo Cliente</span>
                    </a>
                    <a href="vehicles?action=add" class="menu-card menu-card-admin">
                        <span class="label">Nuevo Vehículo</span>
                    </a>
                    <a href="vehicles?action=prepareAssign" class="menu-card menu-card-admin">
                        <span class="label">Vincular Dueño</span>
                    </a>
                    <a href="customers" class="menu-card menu-card-admin">
                        <span class="label">Lista de Clientes</span>
                    </a>
                    <a href="vehicles" class="menu-card menu-card-admin">
                        <span class="label">Lista de Vehículos</span>
                    </a>
                </div>
            </div>

        </div>

        <div class="footer-nav" style="text-align: center; margin: 50px 0;">
            <a href="logout" class="cancel">Cerrar Sesión</a>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            const urlParams = new URLSearchParams(window.location.search);
            const msg = urlParams.get('msg');
            const alerts = {
                'assigned': ['Éxito', 'Dueño vinculado correctamente', 'success'],
                'created':  ['Registrado', 'Los datos se guardaron con éxito', 'success'],
                'success':  ['Hecho', 'Operación completada', 'success'],
                'error':    ['Error', 'Hubo un problema al procesar la solicitud', 'error']
            };
            if (msg && alerts[msg]) {
                Swal.fire(...alerts[msg]);
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        </script>
    </body>
</html>
