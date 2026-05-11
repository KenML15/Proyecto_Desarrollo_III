<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (session.getAttribute("username") == null) { response.sendRedirect("login.jsp"); return; }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role) && !"clerk".equals(role)) { response.sendRedirect("login.jsp"); return; }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Vehículo</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .edit-card {
            background: var(--bg-card);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            padding: 32px 36px;
            width: 100%;
            max-width: 500px;
        }
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; font-size: .82rem; color: var(--text-muted); margin-bottom: 6px; }
        .form-group input[type=text],
        .form-group select {
            width: 100%;
            background: rgba(255,255,255,.06);
            border: 1px solid var(--glass-border);
            border-radius: 8px;
            color: var(--text-main);
            padding: 10px 14px;
            font-size: .95rem;
        }
        .form-group input[type=text]:focus,
        .form-group select:focus { outline: none; border-color: var(--accent); }
        .form-group select option { background: #1e293b; color: var(--text-main); }
        .plate-badge {
            display: inline-block;
            background: var(--accent);
            color: #0f172a;
            font-weight: 700;
            font-size: 1rem;
            padding: 4px 14px;
            border-radius: 6px;
            letter-spacing: 2px;
            margin-bottom: 20px;
        }
        .actions { display: flex; gap: 14px; margin-top: 6px; }
    </style>
</head>
<body>

<div id="titulo">
    <h2>&#x270F; Editar Vehículo</h2>
</div>

<div class="edit-card">
    <div class="plate-badge">${vehicle.plate}</div>

    <form action="vehicles" method="post">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="plate" value="${vehicle.plate}"/>

        <div class="form-group">
            <label>Color</label>
            <input type="text" name="color" value="${vehicle.color}" required placeholder="Ej: Rojo"/>
        </div>

        <div class="form-group">
            <label>Marca</label>
            <input type="text" name="brand" value="${vehicle.brand}" required placeholder="Ej: Toyota"/>
        </div>

        <div class="form-group">
            <label>Modelo</label>
            <input type="text" name="model" value="${vehicle.model}" required placeholder="Ej: Corolla 2020"/>
        </div>

        <div class="form-group">
            <label>Tipo de Vehículo</label>
            <select name="typeId" required>
                <c:forEach var="vt" items="${vehicleTypes}">
                    <option value="${vt.idVehicleType}"
                        <c:if test="${vt.idVehicleType == vehicle.idVehicleType}">selected</c:if>>
                        ${vt.description}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Cliente Asignado</label>
            <select name="idCustomer">
                <option value="0">— Sin cliente —</option>
                <c:forEach var="c" items="${customers}">
                    <option value="${c.id}"
                        <c:if test="${c.id == vehicle.idCustomer}">selected</c:if>>
                        ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="actions">
            <button type="submit" class="btn-entrada">&#x2714; Guardar Cambios</button>
            <a href="vehicles" class="cancel">Cancelar</a>
        </div>
    </form>
</div>

</body>
</html>
