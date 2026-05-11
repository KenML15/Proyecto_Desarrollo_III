<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Recibo de Pago – Parqueo</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        /* ── Receipt ── */
        .receipt {
            background: #fff;
            color: #111;
            width: 340px;
            margin: 0 auto 28px;
            border-radius: 14px;
            padding: 28px 28px 20px;
            font-family: 'Courier New', monospace;
            box-shadow: 0 8px 32px rgba(0,0,0,.35);
        }
        .receipt-header {
            text-align: center;
            border-bottom: 2px dashed #ccc;
            padding-bottom: 14px;
            margin-bottom: 14px;
        }
        .receipt-header h2 { font-size: 1.3rem; margin: 0 0 2px; }
        .receipt-header .sub { font-size: .75rem; color: #555; }
        .receipt-header .ticket-num {
            display: inline-block;
            background: #0f172a;
            color: #4ecca3;
            border-radius: 6px;
            padding: 2px 10px;
            font-size: .78rem;
            margin-top: 6px;
        }
        .r-row {
            display: flex;
            justify-content: space-between;
            padding: 5px 0;
            font-size: .85rem;
            border-bottom: 1px dotted #e2e8f0;
        }
        .r-row .label { color: #555; }
        .r-row .value { font-weight: 700; }
        .r-section-title {
            font-size: .7rem;
            text-transform: uppercase;
            letter-spacing: .1em;
            color: #888;
            margin: 14px 0 6px;
        }
        .total-section {
            border-top: 2px solid #0f172a;
            margin-top: 14px;
            padding-top: 10px;
        }
        .total-section .r-row .value { font-size: 1.3rem; color: #16a34a; }
        .discount-row {
            font-size: .78rem;
            color: #16a34a;
            text-align: right;
            padding: 2px 0 6px;
        }
        .receipt-footer {
            text-align: center;
            font-size: .72rem;
            color: #888;
            border-top: 1px dashed #ccc;
            margin-top: 16px;
            padding-top: 10px;
        }
        /* ── Actions ── */
        .actions-bar {
            display: flex;
            gap: 14px;
            justify-content: center;
            flex-wrap: wrap;
            margin-top: 10px;
        }
        @media print {
            body { background: #fff !important; padding: 0; }
            .no-print { display: none !important; }
            .receipt { box-shadow: none; margin: 0 auto; }
        }
    </style>
</head>
<body>

<div id="titulo" class="no-print">
    <h2>&#x1F9FE; Pago Registrado – Recibo</h2>
</div>

<%-- ── RECEIPT ── --%>
<div class="receipt" id="printableReceipt">
    <div class="receipt-header">
        <h2>${parkingName}</h2>
        <div class="sub">Comprobante de Estacionamiento</div>
        <div class="ticket-num">TIQUETE #${ticket.id}</div>
    </div>

    <div class="r-section-title">Vehículo</div>
    <div class="r-row"><span class="label">Placa</span><span class="value">${ticket.vehicle.plate}</span></div>
    <div class="r-row"><span class="label">Marca / Modelo</span><span class="value">${ticket.vehicle.brand} ${ticket.vehicle.model}</span></div>
    <div class="r-row"><span class="label">Color</span><span class="value">${ticket.vehicle.color}</span></div>
    <div class="r-row"><span class="label">Tipo</span><span class="value">${ticket.vehicle.vehicleTypeDesc}</span></div>

    <div class="r-section-title">Cliente</div>
    <div class="r-row"><span class="label">Nombre</span><span class="value">${ticket.customer.name}</span></div>
    <c:if test="${ticket.customer.disabilityPresented}">
        <div class="r-row"><span class="label">Descuento Ley 7600</span><span class="value" style="color:#16a34a;">50%</span></div>
    </c:if>

    <div class="r-section-title">Parqueo</div>
    <div class="r-row"><span class="label">Parqueo</span><span class="value">${parkingName}</span></div>
    <div class="r-row"><span class="label">Espacio N°</span><span class="value">#${slotNumber}</span></div>

    <div class="r-section-title">Tiempo</div>
    <div class="r-row">
        <span class="label">Entrada</span>
        <span class="value">
${ticket.entryDateFormatted}
        </span>
    </div>
    <div class="r-row">
        <span class="label">Salida</span>
        <span class="value">
            <c:choose>
                <c:when test="${ticket.exitDate != null}">
${ticket.exitDateFormatted}
                </c:when>
                <c:otherwise>Ahora</c:otherwise>
            </c:choose>
        </span>
    </div>
    <div class="r-row">
        <span class="label">Duración</span>
        <span class="value" style="color:#2563eb;">&#x23F1; ${ticket.stayDuration}</span>
    </div>

    <c:if test="${rate != null}">
        <div class="r-section-title">Tarifa Aplicada (${ticket.vehicle.vehicleTypeDesc})</div>
        <div class="r-row"><span class="label">Media hora</span><span class="value">₡<fmt:formatNumber value="${rate.halfHour}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
        <div class="r-row"><span class="label">Hora</span><span class="value">₡<fmt:formatNumber value="${rate.hour}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
        <div class="r-row"><span class="label">Día</span><span class="value">₡<fmt:formatNumber value="${rate.day}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span></div>
    </c:if>

    <div class="total-section">
        <c:if test="${ticket.customer.disabilityPresented}">
            <div class="discount-row">&#x2605; Descuento Ley 7600 (50%) aplicado</div>
        </c:if>
        <div class="r-row">
            <span class="label" style="font-size:1rem;font-weight:700;">TOTAL COBRADO</span>
            <span class="value">₡<fmt:formatNumber value="${totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>
    </div>

    <div class="receipt-footer">
        Gracias por usar nuestros servicios.<br>
    </div>
</div>

<%-- ── ACTIONS ── --%>
<div class="actions-bar no-print">
    <button onclick="window.print()" class="btn-entrada">
        &#x1F5A8; Imprimir Recibo
    </button>
    <a href="tickets" class="btn-salida" style="text-decoration:none; padding:10px 20px; border-radius:8px;">
        &#x2795; Nuevo Tiquete
    </a>
    <a href="${sessionScope.role == 'admin' ? 'menu_admin.jsp' : 'menu_clerk.jsp'}" class="cancel">
        Volver al Menú
    </a>
</div>

</body>
</html>
