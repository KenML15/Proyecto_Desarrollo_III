<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Ocupación en Tiempo Real</title>
    </head>
    <body>

        <div id="titulo">
            <c:choose>
                <c:when test="${not empty lotId}">
                    <h2>Ocupación Actual — ${activeAssignments[0].lotName}</h2>
                </c:when>
                <c:otherwise>
                    <h2>Vehículos en el Parqueo</h2>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Buscador en tiempo real (patrón Lab02 + clases del proyecto) -->
        <div class="search-wrapper">
            <label for="search">Buscar vehículo</label>
            <input type="text" id="search" class="input-search" placeholder="Placa, parqueo...">
        </div>

        <div class="container container--wide">
            <table border="1" id="assignmentTable">
                <thead>
                    <tr id="encabezado">
                        <th>Placa del Vehículo</th>
                        <th>Ubicación (Parqueo)</th>
                        <th>Hora de Entrada</th>
                        <th>Estado Actual</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty activeAssignments}">
                        <tr>
                            <td colspan="5" style="text-align:center; padding: 24px; color: var(--text-muted);">
                                No hay vehículos parqueados en este momento.
                            </td>
                        </tr>
                    </c:if>
                    <c:forEach var="a" items="${activeAssignments}">
                        <tr>
                            <td><strong><c:out value="${a.plateVehicle}"/></strong></td>
                            <td><c:out value="${a.lotName}"/></td>
                            <td><c:out value="${a.entryTime}"/></td>
                            <td>
                                <span class="badge-parked">PARQUEADO</span>
                            </td>
                            <td>
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/tickets?action=viewActive&amp;plate=${a.plateVehicle}"
                                       class="save btn-table btn-table--info">Ver Ticket</a>
                                    <form action="${pageContext.request.contextPath}/tickets" method="POST" style="display:inline;">
                                        <input type="hidden" name="action" value="salida"/>
                                        <input type="hidden" name="plate" value="${a.plateVehicle}"/>
                                        <button type="submit" class="cancel btn-table">&#x2B06; Registrar Salida</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="footer-nav footer-nav--lg">
            <c:choose>
                <c:when test="${not empty lotId}">
                    <a href="parkingLot" class="cancel">← Volver a Gestionar Parqueos</a>
                </c:when>
                <c:otherwise>
                    <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                        ← Volver al Menú
                    </a>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Modal de confirmación de salida de vehículo -->
        <div id="releaseBox" class="confirm-box">
            <div class="confirm-content">
                
                <p>¿Confirmar salida del vehículo <strong><span id="releasePlaca"></span></strong>?</p>
                <div class="confirm-buttons">
                    <button class="btn-modal yes-danger" onclick="releaseYes()">Sí, registrar salida</button>
                    <button class="btn-modal no"         onclick="releaseNo()">Cancelar</button>
                </div>
            </div>
        </div>

        <script src="js/Assignment.js"></script>
    </body>
</html>
