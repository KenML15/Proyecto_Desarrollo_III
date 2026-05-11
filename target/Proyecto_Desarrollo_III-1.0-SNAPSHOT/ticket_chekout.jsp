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
    <title>Checkout – Registrar Salida</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .checkout-wrapper {
            display: flex; gap: 28px; justify-content: center;
            flex-wrap: wrap; width: 100%; max-width: 960px;
        }
        .checkout-card {
            background: var(--bg-card); border: 1px solid var(--glass-border);
            border-radius: 16px; padding: 28px 32px; flex: 1; min-width: 280px;
        }
        .checkout-card h3 {
            color: var(--accent); font-size: 1rem; text-transform: uppercase;
            letter-spacing: .08em; margin-bottom: 18px;
            border-bottom: 1px solid var(--glass-border); padding-bottom: 10px;
        }
        .info-row {
            display: flex; justify-content: space-between;
            padding: 7px 0; font-size: .92rem;
            border-bottom: 1px solid rgba(255,255,255,.04);
        }
        .info-row span:first-child { color: var(--text-muted); }
        .info-row span:last-child  { font-weight: 600; }
        .amount-card {
            background: rgba(78,204,163,.10); border: 2px solid var(--accent);
            border-radius: 14px; padding: 22px 28px; margin-top: 22px;
        }
        .amount-card .lbl {
            font-size: .78rem; color: var(--text-muted);
            text-transform: uppercase; letter-spacing: .08em; margin-bottom: 8px;
        }
        .amount-calculated { font-size: .82rem; color: var(--text-muted); margin-bottom: 10px; }
        .amount-calculated strong { color: var(--accent); }
        .amount-input-row { display: flex; align-items: center; gap: 10px; margin-bottom: 8px; }
        .amount-input-row .currency { font-size: 1.6rem; font-weight: 800; color: var(--accent); }
        .amount-input-row input[type="number"] {
            font-size: 1.6rem; font-weight: 800; color: var(--accent);
            background: transparent; border: none;
            border-bottom: 2px solid var(--accent);
            width: 180px; text-align: right; outline: none; padding: 4px 6px;
        }
        .amount-input-row input[type="number"]:focus { border-bottom-color: #fff; }
        .amount-hint { font-size: .75rem; color: var(--text-muted); font-style: italic; }
        .discount-tag {
            display: inline-block; background: #16a34a22;
            border: 1px solid #16a34a; color: #4ade80;
            border-radius: 20px; padding: 3px 12px; font-size: .78rem; margin-top: 6px;
        }
        .rate-mini-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 4px 16px; margin-top: 4px; }
        .rate-mini-row { display: flex; justify-content: space-between; font-size: .83rem; padding: 4px 0; }
        .rate-mini-row span:first-child { color: var(--text-muted); }
        .btn-set-amount {
            background: rgba(78,204,163,.18); border: 1px solid var(--accent);
            color: var(--accent); border-radius: 6px; padding: 4px 10px;
            font-size: .8rem; cursor: pointer; transition: background .2s;
        }
        .btn-set-amount:hover { background: rgba(78,204,163,.32); }
        .actions-bar {
            display: flex; gap: 14px; justify-content: center;
            margin-top: 28px; flex-wrap: wrap;
        }
        @media print {
            .no-print { display: none !important; }
            body { background: #fff; color: #000; }
            .checkout-card { border: 1px solid #ccc; }
        }
    </style>
</head>
<body>

<div id="titulo">
    <h2>Vista Previa de Salida</h2>
</div>

<div class="checkout-wrapper" id="printableArea">

    <div class="checkout-card">
        <h3>Parqueo</h3>
        <div class="info-row"><span>Parqueo</span><span>${parkingName}</span></div>
        <div class="info-row"><span>Espacio N°</span><span>#${slotNumber}</span></div>

        <h3 style="margin-top:22px;">Vehículo</h3>
        <div class="info-row"><span>Placa</span><span>${ticket.vehicle.plate}</span></div>
        <div class="info-row"><span>Marca / Modelo</span><span>${ticket.vehicle.brand} ${ticket.vehicle.model}</span></div>
        <div class="info-row"><span>Color</span><span>${ticket.vehicle.color}</span></div>
        <div class="info-row"><span>Tipo</span><span>${ticket.vehicle.vehicleTypeDesc}</span></div>

        <h3 style="margin-top:22px;">Cliente</h3>
        <div class="info-row"><span>Nombre</span><span>${ticket.customer.name}</span></div>
        <div class="info-row">
            <span>Discapacidad (Ley 7600)</span>
            <span>
                <c:choose>
                    <c:when test="${ticket.customer.disabilityPresented}">
                        <span style="color:#4ade80;">Sí (50% desc.)</span>
                    </c:when>
                    <c:otherwise>No</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <div class="checkout-card">
        <h3>&#x23F1; Tiempo de Estancia</h3>
        <div class="info-row">
            <span>Entrada</span>
            <span>${ticket.entryDateFormatted}</span>
        </div>
        <div class="info-row">
            <span>Salida</span>
            <span id="exitTimeDisplay">—</span>
        </div>
        <div class="info-row">
            <span>Duración</span>
            <span style="color:#3b82f6;font-weight:700;">&#x23F1; ${ticket.stayDuration}</span>
        </div>

        <h3 style="margin-top:22px;">Tarifas (${ticket.vehicle.vehicleTypeDesc})</h3>
        <c:if test="${rate != null}">
            <div class="rate-mini-grid">
                <div class="rate-mini-row">
                    <span>Media hora</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.halfHour})">
                        CRC <fmt:formatNumber value="${rate.halfHour}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
                <div class="rate-mini-row">
                    <span>1 Hora</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.hour})">
                        CRC <fmt:formatNumber value="${rate.hour}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
                <div class="rate-mini-row">
                    <span>Día</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.day})">
                        CRC <fmt:formatNumber value="${rate.day}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
                <div class="rate-mini-row">
                    <span>Semana</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.week})">
                        CRC <fmt:formatNumber value="${rate.week}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
                <div class="rate-mini-row">
                    <span>Mes</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.month})">
                        CRC <fmt:formatNumber value="${rate.month}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
                <div class="rate-mini-row">
                    <span>Año</span>
                    <button type="button" class="btn-set-amount" onclick="setAmount(${rate.year})">
                        CRC <fmt:formatNumber value="${rate.year}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </button>
                </div>
            </div>
            <p style="font-size:.75rem;color:var(--text-muted);margin-top:8px;">
                Haga clic en una tarifa para aplicarla, o escriba el monto manualmente abajo.
            </p>
        </c:if>

        <div class="amount-card">
            <div class="lbl">Monto Total a Cobrar</div>
            <div class="amount-calculated">
                Calculado automáticamente:
                <strong>CRC <span id="calculatedDisplay">
                    <fmt:formatNumber value="${totalAmount}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                </span></strong>
                <c:if test="${ticket.customer.disabilityPresented}">
                    <span class="discount-tag">&#x2605; 50% Ley 7600 aplicado</span>
                </c:if>
            </div>
            <div class="amount-input-row">
                <span class="currency">CRC </span>
                <input type="number" id="montoEditable" step="0.01" min="0"
                       value="${totalAmount}" oninput="syncMonto(this.value)"
                       title="Puede editar el monto manualmente"/>
            </div>
            <div class="amount-hint">
                Puede editar el monto directamente o usar los botones de tarifa de arriba.
            </div>
        </div>
    </div>

</div>

<form id="payForm" action="tickets" method="POST">
    <input type="hidden" name="action" value="confirmarPago"/>
    <input type="hidden" name="idTicket" value="${ticket.id}"/>
    <input type="hidden" id="montoHidden" name="monto" value="${totalAmount}"/>
    <input type="hidden" name="plate" value="${ticket.vehicle.plate}"/>
</form>

<div class="actions-bar no-print">
    <button onclick="window.print()" class="btn-entrada">
        Imprimir Vista Previa
    </button>
    <button type="button" class="btn-salida" onclick="confirmarPago()">
        Confirmar Pago y Liberar Espacio
    </button>
    <a href="tickets" class="cancel">Cancelar</a>
</div>

<script>
    const now = new Date();
    document.getElementById("exitTimeDisplay").textContent =
        now.toLocaleDateString("es-CR") + " " +
        now.toLocaleTimeString("es-CR", {hour:"2-digit", minute:"2-digit"});

    const hasDisability = ${ticket.customer.disabilityPresented};

    function syncMonto(val) {
        document.getElementById("montoHidden").value = val;
    }

    function setAmount(val) {
        let amount = parseFloat(val);
        if (hasDisability) {
            amount = amount * 0.5;
        }
        const rounded = amount.toFixed(2);
        document.getElementById("montoEditable").value = rounded;
        document.getElementById("montoHidden").value = rounded;
    }

    function confirmarPago() {
        const monto = parseFloat(document.getElementById("montoEditable").value);
        if (isNaN(monto) || monto < 0) {
            alert("Por favor ingrese un monto válido.");
            return;
        }
        document.getElementById("montoHidden").value = monto.toFixed(2);
        if (confirm("\u00bfConfirmar cobro de \u20A1" + monto.toLocaleString("es-CR", {minimumFractionDigits:2}) + " y liberar el espacio?")) {
            document.getElementById("payForm").submit();
        }
    }
</script>

</body>
</html>
