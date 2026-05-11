<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    if (session.getAttribute("username") == null) { response.sendRedirect("login.jsp"); return; }
    if (!"admin".equals(session.getAttribute("role"))) { response.sendRedirect("menu_clerk.jsp"); return; }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tipos de Vehículo y Tarifas</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .page-grid {
            display: grid;
            grid-template-columns: 320px 1fr;
            gap: 28px;
            width: 100%;
            max-width: 1000px;
        }
        @media (max-width: 720px) { .page-grid { grid-template-columns: 1fr; } }

        .panel {
            background: var(--bg-card);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            padding: 24px 26px;
        }
        .panel h3 {
            color: var(--accent);
            font-size: .95rem;
            text-transform: uppercase;
            letter-spacing: .07em;
            margin-bottom: 18px;
            border-bottom: 1px solid var(--glass-border);
            padding-bottom: 10px;
        }
        /* ── Add-type form ── */
        .add-form { display: flex; gap: 10px; margin-bottom: 10px; }
        .add-form input[type=text] {
            flex: 1;
            background: rgba(255,255,255,.06);
            border: 1px solid var(--glass-border);
            border-radius: 8px;
            color: var(--text-main);
            padding: 8px 12px;
            font-size: .9rem;
        }
        .add-form button {
            background: var(--accent);
            color: #0f172a;
            border: none;
            border-radius: 8px;
            padding: 8px 16px;
            font-weight: 700;
            cursor: pointer;
        }
        /* ── Type list ── */
        .type-list { list-style: none; margin-top: 6px; }
        .type-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px 12px;
            border-radius: 8px;
            margin-bottom: 6px;
            background: rgba(255,255,255,.04);
            font-size: .9rem;
        }
        .type-item:hover { background: rgba(255,255,255,.08); }
        .type-item .type-name { font-weight: 600; }
        .type-actions { display: flex; gap: 8px; }
        .btn-edit  { background: rgba(78,204,163,.2); color: var(--accent); border: 1px solid var(--accent); border-radius:6px; padding:4px 10px; font-size:.78rem; cursor:pointer; text-decoration:none; }
        .btn-del   { background: rgba(255,77,77,.15); color: #ff6b6b; border: 1px solid #ff4d4d55; border-radius:6px; padding:4px 10px; font-size:.78rem; cursor:pointer; }
        .btn-rate  { background: rgba(250,204,21,.15); color: #fde047; border: 1px solid #fde04755; border-radius:6px; padding:4px 10px; font-size:.78rem; cursor:pointer; text-decoration:none; }
        /* ── Rates table ── */
        .rates-table { width: 100%; border-collapse: collapse; font-size: .85rem; }
        .rates-table th {
            background: rgba(78,204,163,.12);
            color: var(--accent);
            padding: 9px 12px;
            text-align: left;
            font-weight: 600;
        }
        .rates-table td { padding: 9px 12px; border-bottom: 1px solid var(--glass-border); }
        .rates-table tr:hover td { background: rgba(255,255,255,.04); }
        .no-rate { color: var(--text-muted); font-style: italic; font-size: .82rem; }
        /* ── Alert ── */
        .alert { border-radius: 8px; padding: 10px 16px; margin-bottom: 18px; font-size: .88rem; }
        .alert-success { background: rgba(22,163,74,.15); border: 1px solid #16a34a; color: #4ade80; }
        .alert-error   { background: rgba(239,68,68,.12); border: 1px solid #ef4444; color: #fca5a5; }
    </style>
</head>
<body>

<div class="header-container">
    <img src="IMG/logo.png" alt="Logo" class="logo">
    <div id="titulo">
        <h2>&#x1F697; Tipos de Vehículo y Tarifas</h2>
        <p class="role-badge role-admin">&#x1F6E1; Administrador: ${sessionScope.username}</p>
    </div>
</div>

<%-- ── Alerts ── --%>
<c:if test="${param.success == 'added'}">    <div class="alert alert-success">Tipo de vehículo agregado exitosamente.</div></c:if>
<c:if test="${param.success == 'updated'}">  <div class="alert alert-success">Tipo actualizado correctamente.</div></c:if>
<c:if test="${param.success == 'deleted'}">  <div class="alert alert-success">Tipo eliminado.</div></c:if>
<c:if test="${param.success == 'rate_saved'}"><div class="alert alert-success">Tarifa guardada exitosamente.</div></c:if>
<c:if test="${param.error == 'cannot_delete'}"><div class="alert alert-error">No se puede eliminar: existen vehículos o tarifas asociados a este tipo.</div></c:if>
<c:if test="${param.error == 'empty_name'}"> <div class="alert alert-error">El nombre no puede estar vacío.</div></c:if>

<div class="page-grid">

    <%-- ── LEFT: type list + add form ── --%>
    <div class="panel">
        <h3>&#x2795; Agregar Tipo</h3>
        <form action="vehicleTypes" method="post" class="add-form">
            <input type="hidden" name="action" value="addType"/>
            <input type="text" name="description" placeholder="Ej: Automóvil" required/>
            <button type="submit">Agregar</button>
        </form>

        <h3 style="margin-top:22px;">Lista de Tipos</h3>
        <ul class="type-list">
            <c:forEach var="vt" items="${types}">
                <li class="type-item">
                    <span class="type-name">${vt.description}</span>
                    <div class="type-actions">
                        <a href="vehicleTypes?action=editType&id=${vt.idVehicleType}" class="btn-edit">&#x270F; Editar</a>
                        <a href="vehicleTypes?action=editRate&id=${vt.idVehicleType}" class="btn-rate">&#x1F4B0; Tarifa</a>
                        <form action="vehicleTypes" method="post" style="display:inline;"
                              onsubmit="return confirm('¿Eliminar el tipo «${vt.description}»? Solo es posible si no tiene vehículos ni tarifas asociados.')">
                            <input type="hidden" name="action" value="deleteType"/>
                            <input type="hidden" name="idVehicleType" value="${vt.idVehicleType}"/>
                            <button type="submit" class="btn-del">&#x1F5D1;</button>
                        </form>
                    </div>
                </li>
            </c:forEach>
            <c:if test="${empty types}">
                <li style="color:var(--text-muted); font-size:.88rem; padding:10px;">No hay tipos registrados.</li>
            </c:if>
        </ul>
    </div>

    <%-- ── RIGHT: rates table ── --%>
    <div class="panel">
        <h3>&#x1F4CB; Tarifas Configuradas</h3>
        <table class="rates-table">
            <thead>
                <tr>
                    <th>Tipo de Vehículo</th>
                    <th>½ Hora</th>
                    <th>Hora</th>
                    <th>Día</th>
                    <th>Semana</th>
                    <th>Mes</th>
                    <th>Año</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="vt" items="${types}">
                    <%-- Find rate for this vehicle type --%>
                    <c:set var="vtRate" value="${null}"/>
                    <c:forEach var="r" items="${rates}">
                        <c:if test="${r.idVehicleType == vt.idVehicleType}">
                            <c:set var="vtRate" value="${r}"/>
                        </c:if>
                    </c:forEach>
                    <tr>
                        <td><strong>${vt.description}</strong></td>
                        <c:choose>
                            <c:when test="${vtRate != null}">
                                <td>₡<fmt:formatNumber value="${vtRate.halfHour}" type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>₡<fmt:formatNumber value="${vtRate.hour}"     type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>₡<fmt:formatNumber value="${vtRate.day}"      type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>₡<fmt:formatNumber value="${vtRate.week}"     type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>₡<fmt:formatNumber value="${vtRate.month}"    type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>₡<fmt:formatNumber value="${vtRate.year}"     type="number" minFractionDigits="2" maxFractionDigits="2"/></td>
                            </c:when>
                            <c:otherwise>
                                <td colspan="6" class="no-rate">Sin tarifa configurada</td>
                            </c:otherwise>
                        </c:choose>
                        <td>
                            <a href="vehicleTypes?action=editRate&id=${vt.idVehicleType}" class="btn-rate">
                                &#x270F; ${vtRate != null ? 'Editar' : 'Crear'}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty types}">
                    <tr><td colspan="8" style="color:var(--text-muted); text-align:center; padding:18px;">Agregue tipos de vehículo primero.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

</div>

<div class="footer-nav">
    <a href="menu_admin.jsp" class="cancel">&#x2190; Volver al Menú Admin</a>
</div>

</body>
</html>
