<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Dashboard de Espacios</title>
    </head>
    <body>

        <div id="titulo">
            <h2>Estado de Espacios por Parqueo</h2>
        </div>

        <!-- Buscador en tiempo real (patrón Lab02) -->
        <div class="search-wrapper">
            <label for="search">Buscar parqueo</label>
            <input type="text" id="search" class="input-search" placeholder="Nombre del parqueo...">
        </div>

        <div class="container container--wide">
            <table border="1" id="dashboardTable">
                <thead>
                    <tr id="encabezado">
                        <th>Nombre del Parqueo</th>
                        <th>Capacidad Total</th>
                        <th>Ocupados</th>
                        <th>Disponibles</th>
                        <th class="th-visual">Estado Visual</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${report}">
                        <c:set var="porcentaje" value="${(p.occupatedSpaces * 100) / p.numberOfSpaces}" />
                        <tr>
                            <td><strong><c:out value="${p.name}"/></strong></td>
                            <td class="td-center"><c:out value="${p.numberOfSpaces}"/></td>
                            <td class="td-center"><c:out value="${p.occupatedSpaces}"/></td>
                            <td class="td-center">${p.numberOfSpaces - p.occupatedSpaces}</td>
                            <td class="td-visual">
                                <div class="progress-bar">
                                    <div class="${p.occupatedSpaces >= p.numberOfSpaces
                                                    ? 'progress-bar__fill progress-bar__fill--full'
                                                    : 'progress-bar__fill progress-bar__fill--ok'}"
                                         style="width: ${porcentaje}%;">
                                    </div>
                                </div>
                                <small class="progress-label">
                                    <fmt:formatNumber value="${porcentaje}" maxFractionDigits="1"/>% ocupado
                                </small>
                            </td>
                            <td>
                                <a href="assignments?action=board&id=${p.id}&name=${p.name}"
                                   class="save btn-table btn-table--info">Ver Tablero</a>
                                <a href="assignments?action=manageSlots&id=${p.id}"
                                   class="save btn-table">Configurar Espacios</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav footer-nav--lg">
            <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                ← Volver al Menú
            </a>
        </div>

        <script>
            // Buscador en tiempo real
            document.getElementById("search").addEventListener("keyup", function () {
                var filter = this.value.toLowerCase();
                document.querySelectorAll("#dashboardTable tbody tr").forEach(function (row) {
                    row.style.display = row.textContent.toLowerCase().includes(filter) ? "" : "none";
                });
            });
        </script>
    </body>
</html>
