<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Protección de sesión --%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Si es admin y entra aquí, enviarlo al menú admin
    if ("admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("menu_admin.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Menú Clerk</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css"/>
    </head>

    <body>

        <div class="header-container">
            <img src="IMG/logo.png" alt="Logo" class="logo">

            <div id="titulo">
                <h2>Panel de Operaciones</h2>
                <p class="role-badge role-clerk">
                    &#x1F464; Clerk: ${sessionScope.username}
                </p>
            </div>
        </div>

        <%-- Mensaje de acceso denegado --%>
        <c:if test="${not empty sessionScope.accessError}">
            <p class="error-msg">${sessionScope.accessError}</p>
            <% session.removeAttribute("accessError");%>
        </c:if>

        <div class="menu-container">

            <div class="menu-section">
                <h3>Registros</h3>

                <div class="grid-menu">

                    <a href="insert_customer.jsp" class="menu-card">
                        <span class="label">Insertar Cliente</span>
                    </a>

                    <a href="vehicles?action=add" class="menu-card">
                        <span class="label">Insertar Vehículo</span>
                    </a>

                    <a href="vehicles?action=prepareAssign"
                       class="menu-card"
                       >

                        <span class="label">
                            Vincular Dueño a Vehículo
                        </span>
                    </a>

                    <a href="assignments?action=prepare" class="menu-card">
                        <span class="label">
                            Ingresar Vehículo a Parqueo
                        </span>
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

        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
            const urlParams = new URLSearchParams(window.location.search);

            if (urlParams.get('msg') === 'assigned') {
                Swal.fire(
                        '¡Éxito!',
                        'Dueño vinculado correctamente',
                        'success'
                        );
            }

            if (urlParams.get('msg') === 'created') {
                Swal.fire(
                        '¡Éxito!',
                        'Vehículo registrado',
                        'success'
                        );
            }
        </script>

        <div class="logout-container">
            <a href="logout" class="btn-logout">
                Cerrar Sesión
            </a>
        </div>

    </body>
</html>