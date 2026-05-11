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
    <title>Editar Tipo de Vehículo</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        .edit-card {
            background: var(--bg-card);
            border: 1px solid var(--glass-border);
            border-radius: 16px;
            padding: 32px 36px;
            width: 100%;
            max-width: 420px;
        }
        .edit-card h3 {
            color: var(--accent);
            font-size: .95rem;
            text-transform: uppercase;
            letter-spacing: .07em;
            margin-bottom: 22px;
            border-bottom: 1px solid var(--glass-border);
            padding-bottom: 10px;
        }
        .form-group label {
            display: block;
            font-size: .82rem;
            color: var(--text-muted);
            margin-bottom: 6px;
        }
        .form-group input[type=text] {
            width: 100%;
            background: rgba(255,255,255,.06);
            border: 1px solid var(--glass-border);
            border-radius: 8px;
            color: var(--text-main);
            padding: 10px 12px;
            font-size: .9rem;
            box-sizing: border-box;
        }
        .form-group input[type=text]:focus {
            outline: none;
            border-color: var(--accent);
        }
        .actions { display: flex; gap: 14px; margin-top: 26px; }
    </style>
</head>
<body>

<div id="titulo">
    <h2>Editar Tipo de Vehículo</h2>
</div>

<div class="edit-card">
    <h3>Modificar descripción</h3>
    <form action="vehicleTypes" method="post">
        <input type="hidden" name="action" value="updateType"/>
        <input type="hidden" name="idVehicleType" value="${vehicleType.idVehicleType}"/>
        <div class="form-group">
            <label>Nombre del tipo</label>
            <input type="text" name="description" value="${vehicleType.description}" required/>
        </div>
        <div class="actions">
            <button type="submit" class="btn-entrada">Guardar</button>
            <a href="vehicleTypes" class="cancel">Cancelar</a>
        </div>
    </form>
</div>

</body>
</html>
