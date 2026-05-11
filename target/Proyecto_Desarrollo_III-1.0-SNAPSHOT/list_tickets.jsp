<%-- 
    Document   : list_tickets
    Author     : Jefferson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%-- Protección de sesión --%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tiquetes de Parqueo</title>
        <link rel="stylesheet" href="CSS/style.css"/>
        <style>
            /* ── Ticket table ── */
            .ticket-table {
                width: 100%;
                border-collapse: collapse;
                font-size: .88rem;
                margin-top: 10px;
            }
            .ticket-table thead tr {
                background: #0f172a;
                color: #fff;
            }
            .ticket-table thead th {
                padding: 11px 14px;
                text-align: left;
                font-weight: 600;
                letter-spacing: .04em;
                border-bottom: 2px solid #4ecca3;
                white-space: nowrap;
            }
            .ticket-table tbody tr {
                border-bottom: 1px solid rgba(255,255,255,.06);
                transition: background .15s;
            }
            .ticket-table tbody tr:nth-child(even) {
                background: rgba(255,255,255,.03);
            }
            .ticket-table tbody tr:hover {
                background: rgba(78,204,163,.08);
            }
            .ticket-table td {
                padding: 10px 14px;
                vertical-align: middle;
            }
            /* row highlighted after new entry */
            .highlight-row td {
                background: rgba(78,204,163,.14) !important;
            }
            /* ticket id badge */
            .ticket-id {
                background: #0f172a;
                color: #4ecca3;
                border-radius: 6px;
                padding: 2px 8px;
                font-size: .78rem;
                font-weight: 700;
                white-space: nowrap;
            }
            /* status badges */
            .badge-active {
                display: inline-block;
                background: #16a34a22;
                border: 1px solid #16a34a;
                color: #4ade80;
                border-radius: 20px;
                padding: 2px 10px;
                font-size: .75rem;
                font-weight: 700;
            }
            .badge-closed {
                display: inline-block;
                background: rgba(255,255,255,.06);
                border: 1px solid rgba(255,255,255,.15);
                color: #94a3b8;
                border-radius: 20px;
                padding: 2px 10px;
                font-size: .75rem;
            }
            .badge.badge-warning {
                display: inline-block;
                background: #92400e22;
                border: 1px solid #d97706;
                color: #fbbf24;
                border-radius: 20px;
                padding: 2px 10px;
                font-size: .75rem;
            }
            /* amount cells */
            .amount-paid {
                font-weight: 700;
                color: #4ade80;
            }
            .amount-pending {
                color: #64748b;
            }
            /* duration cell */
            .duration-cell {
                color: #60a5fa;
                font-weight: 600;
                white-space: nowrap;
            }
            /* container */
            .container--tickets {
                max-width: 1100px;
                margin: 0 auto;
                padding: 0 16px 40px;
                overflow-x: auto;
            }
            .section-title {
                font-size: 1rem;
                font-weight: 700;
                margin: 20px 0 8px;
                color: var(--accent, #4ecca3);
            }
            .empty-msg {
                text-align: center;
                padding: 40px;
                color: #64748b;
            }
        </style>

    </head>
    <body>

        <div id="titulo">
            <h2>Gestión de Tiquetes</h2>
        </div>

        <%-- ── ALERTAS de resultado ── --%>
        <c:if test="${param.success == '1'}">
            <div class="alert-box alert-success">
                Entrada registrada exitosamente. ID de tiquete: <strong>#${param.ticketId}</strong>
            </div>
        </c:if>
        <c:if test="${param.exit == '1'}">
            <div class="alert-box alert-warning">
                Salida registrada. Monto cobrado:
                <strong>
                    ₡<fmt:formatNumber value="${param.monto}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                </strong>
            </div>
        </c:if>
        <c:if test="${param.error == '1'}">
            <div class="alert-box alert-error">
                Error al registrar la entrada. Verifique los datos e intente de nuevo.
            </div>
        </c:if>
        <c:if test="${param.error == 'not_found'}">
            <div class="alert-box alert-error">
                No se encontró tiquete activo para esa placa.
            </div>
        </c:if>
        <c:if test="${param.error == 'exit_failed'}">
            <div class="alert-box alert-error">
                Error al procesar la salida. Intente de nuevo.
            </div>
        </c:if>
        <c:if test="${param.error == 'processing_error'}">
            <div class="alert-box alert-error">
                Error de procesamiento. Verifique que el vehículo tiene tarifa configurada.
            </div>
        </c:if>
        <c:if test="${param.error == 'invalid_data'}">
            <div class="alert-box alert-error">
                Datos inválidos. Verifique el formulario.
            </div>
        </c:if>

        <%-- ── FORMULARIO: Solo Salida (entrada es automática al asignar vehículo) ── --%>
        <div class="tickets-grid" style="justify-content:center;">

            <div class="ticket-form-card">
                <h3>&#x2B06; Registrar Salida y Cobro</h3>
                <p style="font-size:.82rem;color:#888;margin-bottom:12px;">
                    La entrada se registra automáticamente cuando el operador asigna el vehículo al parqueo.
                </p>
                <form action="tickets" method="post">
                    <input type="hidden" name="action" value="salida"/>

                    <div class="form-group">
                        <label for="plateSalida">Placa del Vehículo</label>
                        <input type="text" id="plateSalida" name="plate"
                               placeholder="Ej: ABC-123" maxlength="10"
                               style="text-transform:uppercase" required/>
                        <small style="color:#888; font-size:.78rem;">
                            Se calculará el monto según la tarifa del tipo de vehículo
                        </small>
                    </div>

                    <div style="background:#fef9e7; border:1px solid #f39c12; border-radius:6px;
                         padding:10px 14px; margin-bottom:14px; font-size:.83rem; color:#7d6608;">
                        El sistema aplicará <strong>50% de descuento</strong> automáticamente
                        si el cliente tiene discapacidad registrada (Ley 7600).
                    </div>

                    <button type="submit" class="btn-salida">
                        &#x2B06; Registrar Salida y Cobrar
                    </button>
                </form>
            </div>

        </div>

                <%-- ── TABLA DE TIQUETES ── --%>
        <%--
            El TicketController no hace doGet aún, así que esta tabla
            se muestra cuando el controlador envíe la lista.
            Mientras tanto, se muestra un mensaje informativo.
        --%>
        <div class="container--tickets">
            <h3 class="section-title">Historial de Tiquetes</h3>

            <c:choose>
                <c:when test="${not empty tickets}">
                    <table class="ticket-table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Placa</th>
                                <th>Cliente</th>
                                <th>Entrada</th>
                                <th>Salida</th>
                                <th>Duración</th>
                                <th>Monto</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${tickets}">
                                <tr class="${t.id == param.ticketId ? 'highlight-row' : ''}">
                                    <td><span class="ticket-id">#${t.id}</span></td>
                                    <td><strong><c:out value="${t.vehicle.plate}"/></strong></td>
                                    <td><c:out value="${t.customer.name}"/></td>
                                    <td>
                                        <c:out value="${t.entryDateFormatted}"/>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty t.exitDate}">
                                                <c:out value="${t.exitDateFormatted}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge badge-warning">En parqueo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="duration-cell">
                                        &#x23F1; ${t.stayDuration}
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.totalAmount > 0}">
                                                <span class="amount-paid">
                                                    ₡<fmt:formatNumber value="${t.totalAmount}"
                                                                      type="number" minFractionDigits="2" maxFractionDigits="2"/>
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="amount-pending">—</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${empty t.exitDate}">
                                                <span class="badge-active">ACTIVO</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-closed">CERRADO</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="empty-msg">
                        No hay tiquetes para mostrar.<br>
                        <small>Use los formularios de arriba para registrar entradas y salidas.</small>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="footer-nav">
            <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                Volver al Menú
            </a>
        </div>

    </body>
</html>

