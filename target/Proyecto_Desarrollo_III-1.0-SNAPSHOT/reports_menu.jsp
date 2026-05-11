<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp"); return;
    }
    if (!"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("menu_clerk.jsp"); return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reportes – Sistema de Parqueos</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .reports-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
            max-width: 920px;
            margin: 32px auto;
            padding: 0 16px;
        }
        .report-card {
            background: var(--bg-card);
            border: 1px solid var(--glass-border);
            border-radius: 18px;
            padding: 28px 26px 22px;
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 12px;
            transition: transform .2s, box-shadow .2s;
        }
        .report-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 12px 32px rgba(78,204,163,.15);
        }
        .report-icon {
            font-size: 2.4rem;
            line-height: 1;
        }
        .report-card h3 {
            margin: 0;
            font-size: 1.05rem;
            color: var(--accent);
        }
        .report-card p {
            margin: 0;
            font-size: .87rem;
            color: var(--text-muted);
            line-height: 1.5;
        }
        .btn-report {
            margin-top: auto;
            padding: 9px 22px;
            background: var(--accent);
            color: #0f172a;
            border: none;
            border-radius: 8px;
            font-weight: 700;
            font-size: .88rem;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            transition: opacity .18s;
        }
        .btn-report:hover { opacity: .85; }
    </style>
</head>
<body>

<div id="titulo">
    <h2>&#x1F4CA; Generación de Reportes PDF</h2>
</div>

<div class="reports-grid">

    <!-- Reporte 1: Ingresos -->
    <div class="report-card">
        <div class="report-icon">&#x1F4B0;</div>
        <h3>Reporte de Ingresos</h3>
        <p>
            Muestra el total de dinero cobrado en el sistema, desglosado por tiquete.
            Incluye descuentos por Ley 7600 y totales generales.
        </p>
        <a href="reports?report=income" target="_blank" class="btn-report">
            &#x1F4C4; Generar PDF
        </a>
    </div>

    <!-- Reporte 2: Ocupación -->
    <div class="report-card">
        <div class="report-icon">&#x1F3E2;</div>
        <h3>Reporte de Ocupación</h3>
        <p>
            Estado actual de todos los parqueos: espacios totales, cuántos están
            ocupados, disponibles y porcentaje de ocupación.
        </p>
        <a href="reports?report=occupancy" target="_blank" class="btn-report">
            &#x1F4C4; Generar PDF
        </a>
    </div>

    <!-- Reporte 3: Historial de tiquetes -->
    <div class="report-card">
        <div class="report-icon">&#x1F9FE;</div>
        <h3>Historial de Tiquetes</h3>
        <p>
            Listado completo de tiquetes cerrados con placa, cliente, tipo de vehículo,
            duración de estancia, monto cobrado y aplicación de descuentos.
        </p>
        <a href="reports?report=tickets" target="_blank" class="btn-report">
            &#x1F4C4; Generar PDF
        </a>
    </div>

</div>

<div class="footer-nav" style="text-align:center; margin-top:24px;">
    <a href="menu_admin.jsp" class="cancel">&#x2190; Volver al Menú</a>
</div>

</body>
</html>
