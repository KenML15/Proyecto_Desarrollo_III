<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (session.getAttribute("username") == null) { response.sendRedirect("login.jsp"); return; }
    if (!"admin".equals(session.getAttribute("role"))) { response.sendRedirect("menu_clerk.jsp"); return; }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Tarifa – ${vehicleType.description}</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .rate-card {
            background: var(--bg-card);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            padding: 32px 36px;
            width: 100%;
            max-width: 520px;
        }
        .rate-card h3 {
            color: var(--accent);
            font-size: .95rem;
            text-transform: uppercase;
            letter-spacing: .07em;
            margin-bottom: 22px;
            border-bottom: 1px solid var(--glass-border);
            padding-bottom: 10px;
        }
        .rate-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px 20px;
        }
        .form-group label {
            display: block;
            font-size: .82rem;
            color: var(--text-muted);
            margin-bottom: 5px;
        }
        .form-group input[type=number] {
            width: 100%;
            background: rgba(255,255,255,.06);
            border: 1px solid var(--glass-border);
            border-radius: 8px;
            color: var(--text-main);
            padding: 9px 12px;
            font-size: .9rem;
        }
        .form-group input[type=number]:focus {
            outline: none;
            border-color: var(--accent);
        }
        .type-badge {
            display: inline-block;
            background: rgba(78,204,163,.15);
            border: 1px solid var(--accent);
            color: var(--accent);
            border-radius: 20px;
            padding: 4px 14px;
            font-size: .85rem;
            font-weight: 600;
            margin-bottom: 22px;
        }
        .actions { display: flex; gap: 14px; margin-top: 26px; }
    </style>
</head>
<body>

<div id="titulo">
    <h2>Configurar Tarifa</h2>
</div>

<div class="rate-card">
    <div class="type-badge">${vehicleType.description}</div>

    <form action="vehicleTypes" method="post">
        <input type="hidden" name="action" value="saveRate"/>
        <input type="hidden" name="idVehicleType" value="${vehicleType.idVehicleType}"/>

        <h3>Montos en Colones (₡)</h3>
        <div class="rate-grid">
            <div class="form-group">
                <label>Media Hora</label>
                <input type="number" name="halfHour" step="0.01" min="0"
                       value="${rate.halfHour > 0 ? rate.halfHour : 0}" required/>
            </div>
            <div class="form-group">
                <label>Hora Completa</label>
                <input type="number" name="hour" step="0.01" min="0"
                       value="${rate.hour > 0 ? rate.hour : 0}" required/>
            </div>
            <div class="form-group">
                <label>Día</label>
                <input type="number" name="day" step="0.01" min="0"
                       value="${rate.day > 0 ? rate.day : 0}" required/>
            </div>
            <div class="form-group">
                <label>Semana</label>
                <input type="number" name="week" step="0.01" min="0"
                       value="${rate.week > 0 ? rate.week : 0}" required/>
            </div>
            <div class="form-group">
                <label>Mes</label>
                <input type="number" name="month" step="0.01" min="0"
                       value="${rate.month > 0 ? rate.month : 0}" required/>
            </div>
            <div class="form-group">
                <label>Año</label>
                <input type="number" name="year" step="0.01" min="0"
                       value="${rate.year > 0 ? rate.year : 0}" required/>
            </div>
        </div>

        <div class="actions">
            <button type="submit" class="btn-entrada">&#x2714; Guardar Tarifa</button>
            <a href="vehicleTypes" class="cancel">Cancelar</a>
        </div>
    </form>
</div>

</body>
</html>
