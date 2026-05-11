<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); return;
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role) && !"clerk".equals(role)) {
        response.sendRedirect("login.jsp"); return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tiquete de Parqueo</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        /* ── Ticket / Recibo ── */
        .ticket-wrapper {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 24px 16px 40px;
        }
        .ticket {
            background: #fff;
            color: #111;
            width: 360px;
            border-radius: 16px;
            padding: 28px 28px 22px;
            font-family: 'Courier New', monospace;
            box-shadow: 0 8px 36px rgba(0,0,0,.38);
            position: relative;
        }
        /* Notch effect */
        .ticket::before, .ticket::after {
            content: '';
            position: absolute;
            width: 24px; height: 24px;
            background: #0f172a;
            border-radius: 50%;
            top: 50%;
            transform: translateY(-50%);
        }
        .ticket::before { left: -12px; }
        .ticket::after  { right: -12px; }

        .ticket-header {
            text-align: center;
            border-bottom: 2px dashed #ccc;
            padding-bottom: 14px;
            margin-bottom: 14px;
        }
        .ticket-header .parking-name {
            font-size: 1.25rem;
            font-weight: 800;
            margin: 0 0 2px;
        }
        .ticket-header .sub {
            font-size: .72rem;
            color: #666;
        }
        .ticket-num {
            display: inline-block;
            background: #0f172a;
            color: #4ecca3;
            border-radius: 6px;
            padding: 3px 12px;
            font-size: .8rem;
            margin-top: 6px;
            letter-spacing: .05em;
        }
        .status-badge {
            display: inline-block;
            border-radius: 20px;
            padding: 3px 14px;
            font-size: .75rem;
            font-weight: 700;
            margin-top: 8px;
            letter-spacing: .06em;
        }
        .status-active {
            background: #dcfce7;
            color: #16a34a;
            border: 1px solid #16a34a;
        }
        .status-closed {
            background: #f1f5f9;
            color: #475569;
            border: 1px solid #cbd5e1;
        }

        .section-title {
            font-size: .68rem;
            text-transform: uppercase;
            letter-spacing: .1em;
            color: #888;
            margin: 14px 0 6px;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 5px 0;
            font-size: .84rem;
            border-bottom: 1px dotted #e2e8f0;
        }
        .info-row .lbl { color: #555; }
        .info-row .val { font-weight: 700; }

        .dashed-line {
            border: none;
            border-top: 2px dashed #e2e8f0;
            margin: 14px 0;
        }

        .total-box {
            background: #f0fdf4;
            border: 1px solid #16a34a;
            border-radius: 10px;
            padding: 14px 18px;
            text-align: center;
            margin-top: 14px;
        }
        .total-box .total-lbl {
            font-size: .75rem;
            color: #666;
            margin-bottom: 2px;
        }
        .total-box .total-amt {
            font-size: 1.8rem;
            font-weight: 800;
            color: #16a34a;
        }
        .total-box .pending-lbl {
            font-size: .78rem;
            color: #0f172a;
            font-style: italic;
        }
        .discount-tag {
            display: inline-block;
            background: #dcfce722;
            border: 1px solid #16a34a;
            color: #16a34a;
            border-radius: 20px;
            padding: 2px 10px;
            font-size: .72rem;
            margin-top: 4px;
        }

        .ticket-footer {
            text-align: center;
            font-size: .68rem;
            color: #999;
            border-top: 1px dashed #ddd;
            margin-top: 16px;
            padding-top: 10px;
            line-height: 1.5;
        }

        /* ── Actions ── */
        .actions-bar {
            display: flex;
            gap: 14px;
            justify-content: center;
            flex-wrap: wrap;
            margin-top: 28px;
        }

        /* ── Rate mini table ── */
        .rate-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 4px 14px;
            margin-top: 4px;
        }
        .rate-row {
            display: flex;
            justify-content: space-between;
            font-size: .8rem;
            padding: 3px 0;
        }
        .rate-row .rl { color: #666; }

        @media print {
            body { background: #fff !important; padding: 0; }
            .no-print { display: none !important; }
            .ticket { box-shadow: none; margin: 0 auto; }
            .ticket::before, .ticket::after { display: none; }
        }
    </style>
</head>
<body>

<div id="titulo" class="no-print">
    <h2>
        <c:choose>
            <c:when test="${entryOnly}">&#x2705; Vehículo Ingresado – Tiquete de Entrada</c:when>
            <c:otherwise>&#x1F3AB; Tiquete Activo – Vehículo en Parqueo</c:otherwise>
        </c:choose>
    </h2>
</div>

<div class="ticket-wrapper">

    <c:choose>
        <c:when test="${ticket != null}">

            <div class="ticket" id="printableTicket">

                <%-- ── HEADER ── --%>
                <div class="ticket-header">
                    <div class="parking-name">${parkingName}</div>
                    <div class="sub">Tiquete de Estacionamiento</div>
                    <div class="ticket-num">TIQUETE #${ticket.id}</div><br>
                    <c:choose>
                        <c:when test="${entryOnly}">
                            <span class="status-badge status-active">&#x1F7E2; ACTIVO – EN PARQUEO</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-active">&#x1F7E2; EN PARQUEO</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- ── PARQUEO ── --%>
                <div class="section-title">&#x1F3E2; Ubicación</div>
                <div class="info-row">
                    <span class="lbl">Parqueo</span>
                    <span class="val">${parkingName}</span>
                </div>
                <div class="info-row">
                    <span class="lbl">Espacio N°</span>
                    <span class="val">#${slotNumber}</span>
                </div>

                <hr class="dashed-line"/>

                <%-- ── VEHÍCULO ── --%>
                <div class="section-title">&#x1F697; Vehículo</div>
                <div class="info-row">
                    <span class="lbl">Placa</span>
                    <span class="val">${ticket.vehicle.plate}</span>
                </div>
                <div class="info-row">
                    <span class="lbl">Marca / Modelo</span>
                    <span class="val">${ticket.vehicle.brand} ${ticket.vehicle.model}</span>
                </div>
                <div class="info-row">
                    <span class="lbl">Color</span>
                    <span class="val">${ticket.vehicle.color}</span>
                </div>
                <div class="info-row">
                    <span class="lbl">Tipo</span>
                    <span class="val">${ticket.vehicle.vehicleTypeDesc}</span>
                </div>

                <hr class="dashed-line"/>

                <%-- ── CLIENTE ── --%>
                <c:if test="${ticket.customer != null}">
                    <div class="section-title">&#x1F464; Cliente</div>
                    <div class="info-row">
                        <span class="lbl">Nombre</span>
                        <span class="val">${ticket.customer.name}</span>
                    </div>
                    <c:if test="${ticket.customer.disabilityPresented}">
                        <div class="info-row">
                            <span class="lbl">Discapacidad</span>
                            <span class="val" style="color:#16a34a;">&#10003; Desc. 50% (Ley 7600)</span>
                        </div>
                    </c:if>
                </c:if>

                <hr class="dashed-line"/>

                <%-- ── TIEMPOS ── --%>
                <div class="section-title">&#x23F1; Tiempos</div>
                <div class="info-row">
                    <span class="lbl">Entrada</span>
                    <span class="val">${ticket.entryDateFormatted}</span>
                </div>
                <div class="info-row">
                    <span class="lbl">Salida</span>
                    <span class="val">
                        <c:choose>
                            <c:when test="${ticket.exitDate != null}">${ticket.exitDateFormatted}</c:when>
                            <c:otherwise><em style="color:#888;">Pendiente</em></c:otherwise>
                        </c:choose>
                    </span>
                </div>
                <div class="info-row">
                    <span class="lbl">Tiempo en parqueo</span>
                    <span class="val" style="color:#3b82f6;">&#x23F1; ${ticket.stayDuration}</span>
                </div>

                <%-- ── TARIFA ── --%>
                <c:if test="${rate != null}">
                    <hr class="dashed-line"/>
                    <div class="section-title">&#x1F4B0; Tarifa por hora (${ticket.vehicle.vehicleTypeDesc})</div>
                    <div class="rate-grid">
                        <div class="rate-row"><span class="rl">Media hora</span><span>&#x20A1;<fmt:formatNumber value="${rate.halfHour}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                        <div class="rate-row"><span class="rl">1 Hora</span><span>&#x20A1;<fmt:formatNumber value="${rate.hour}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                        <div class="rate-row"><span class="rl">Día</span><span>&#x20A1;<fmt:formatNumber value="${rate.day}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                        <div class="rate-row"><span class="rl">Semana</span><span>&#x20A1;<fmt:formatNumber value="${rate.week}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                        <div class="rate-row"><span class="rl">Mes</span><span>&#x20A1;<fmt:formatNumber value="${rate.month}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                        <div class="rate-row"><span class="rl">Año</span><span>&#x20A1;<fmt:formatNumber value="${rate.year}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
                    </div>
                </c:if>

                <%-- ── MONTO ACUMULADO (si no es solo entrada) ── --%>
                <c:if test="${!entryOnly && totalAmount > 0}">
                    <div class="total-box">
                        <div class="total-lbl">MONTO ACUMULADO HASTA AHORA</div>
                        <div class="total-amt">&#x20A1;<fmt:formatNumber value="${totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></div>
                        <c:if test="${ticket.customer.disabilityPresented}">
                            <span class="discount-tag">&#x2605; 50% desc. Ley 7600 incluido</span>
                        </c:if>
                    </div>
                </c:if>

                <c:if test="${entryOnly}">
                    <div class="total-box" style="background:#eff6ff; border-color:#3b82f6;">
                        <div class="pending-lbl">&#x23F3; Cobro pendiente al momento de salida</div>
                    </div>
                </c:if>

                <%-- ── FOOTER ── --%>
                <div class="ticket-footer">
                    Gracias por usar nuestros servicios.<br>
                    Conserve este tiquete para el cobro a la salida.
                </div>

            </div><%-- /ticket --%>

            <%-- ── ACCIONES ── --%>
            <div class="actions-bar no-print">

                <%-- Imprimir --%>
                <button onclick="window.print()" class="btn-entrada">
                    &#x1F5A8; Imprimir Tiquete
                </button>

                <%-- Registrar Salida (solo si está activo) --%>
                <c:if test="${ticket.exitDate == null}">
                    <form action="tickets" method="POST" style="display:inline;">
                        <input type="hidden" name="action" value="salida"/>
                        <input type="hidden" name="plate" value="${ticket.vehicle.plate}"/>
                        <button type="submit" class="btn-salida">
                            &#x2B06; Registrar Salida y Cobrar
                        </button>
                    </form>
                </c:if>

                <%-- Ver historial --%>
                <a href="tickets" class="save" style="text-decoration:none; padding:10px 20px; border-radius:8px;">
                    &#x1F4CB; Ver Historial
                </a>

                <%-- Volver --%>
                <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
                    Volver al Menú
                </a>

            </div>

        </c:when>
        <c:otherwise>
            <div class="empty-msg" style="margin-top:40px;">
                <p>No se encontró información del tiquete.</p>
                <a href="assignments?action=list" class="cancel">Volver a Ocupación</a>
            </div>
        </c:otherwise>
    </c:choose>

</div><%-- /ticket-wrapper --%>

</body>
</html>
