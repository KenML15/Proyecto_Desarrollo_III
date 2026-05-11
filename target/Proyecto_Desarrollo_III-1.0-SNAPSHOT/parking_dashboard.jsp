<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Dashboard de Espacios | Sistema de Parqueo</title>
        <style>
            /* Estilos locales para asegurar la visualización de la barra de progreso */
            .progress-bar {
                background: rgba(255,255,255,0.1);
                border-radius: 10px;
                height: 12px;
                width: 100%;
                overflow: hidden;
                margin-bottom: 4px;
            }
            .progress-bar__fill { height: 100%; transition: width 0.5s ease-in-out; }
            .progress-bar__fill--ok { background: #2ecc71; }
            .progress-bar__fill--full { background: #e74c3c; }
            .progress-label { font-size: 0.75rem; color: #bdc3c7; }
            .btn-group { display: flex; gap: 8px; flex-wrap: wrap; }
            .btn-table { padding: 6px 12px; border-radius: 4px; font-size: 0.85rem; text-decoration: none; display: inline-block; }
            .btn-table--info { background-color: #3498db; color: white; }
        </style>
    </head>
    <body>

        <div id="titulo">
            <h2>Estado de Espacios por Parqueo</h2>
        </div>

        <div class="search-wrapper" style="max-width: 400px; margin: 20px auto; text-align: center;">
            <label for="search" style="display: block; margin-bottom: 8px;">Buscar parqueo:</label>
            <input type="text" id="search" class="input-search" placeholder="Escriba el nombre..." 
                   style="width: 100%; padding: 10px; border-radius: 20px; border: 1px solid #ccc;">
        </div>

        <div class="container container--wide">
            <table border="1" id="dashboardTable">
                <thead>
                    <tr id="encabezado">
                        <th>Nombre del Parqueo</th>
                        <th>Capacidad Total</th>
                        <th>Espacios Ocupados</th>
                        <th>Disponibles</th>
                        <th class="th-visual">Estado Visual</th>
                        <th>Acciones de Gestión</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${report}">
                        <%-- Cálculo del porcentaje de ocupación --%>
                        <c:set var="porcentaje" value="${(p.occupatedSpaces * 100) / p.numberOfSpaces}" />
                        
                        <tr>
                            <td><strong><c:out value="${p.name}"/></strong></td>
                            <td class="td-center">${p.numberOfSpaces}</td>
                            <td class="td-center">${p.occupatedSpaces}</td>
                            <td class="td-center">
                                ${p.numberOfSpaces - p.occupatedSpaces}
                            </td>
                            <td class="td-visual" style="width: 200px;">
                                <div class="progress-bar">
                                    <div class="${p.occupatedSpaces >= p.numberOfSpaces ? 'progress-bar__fill progress-bar__fill--full' : 'progress-bar__fill progress-bar__fill--ok'}"
                                         style="width: ${porcentaje}%;">
                                    </div>
                                </div>
                                <small class="progress-label">
                                    <fmt:formatNumber value="${porcentaje}" maxFractionDigits="1" />% ocupado
                                </small>
                            </td>
                            <td>
                                <div class="btn-group">
                                    <a href="assignments?action=board&id=${p.id}&name=${p.name}" class="btn-table btn-table--info">
                                        Ver Tablero
                                    </a>
                                    <a href="assignments?action=manageSlots&id=${p.id}" class="save btn-table">
                                        Configurar
                                    </a>
                                    <a href="assignments?action=list&lotId=${p.id}" class="btn-table btn-table--info" style="background-color: #9b59b6;">
                                        Tiquetes
                                    </a>
                                    <a href="vehicles?action=list" class="btn-table" style="background-color: #f39c12; color: white;">
                                        Vehículos
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav footer-nav--lg" style="margin-top: 30px; text-align: center;">
            <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                ← Volver al Menú Principal
            </a>
        </div>

        <script>
            // Lógica del buscador (filtra por texto en cualquier columna)
            document.getElementById("search").addEventListener("keyup", function () {
                var filter = this.value.toLowerCase();
                var rows = document.querySelectorAll("#dashboardTable tbody tr");
                
                rows.forEach(function (row) {
                    var text = row.innerText.toLowerCase();
                    row.style.display = text.includes(filter) ? "" : "none";
                });
            });
        </script>
    </body>
</html>