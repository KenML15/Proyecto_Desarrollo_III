<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Seguridad: Verificación de sesión y roles (Admin o Clerk) [cite: 29, 37]
    if (session.getAttribute("username") == null) { 
        response.sendRedirect("login.jsp"); 
        return; 
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role) && !"clerk".equals(role)) { 
        response.sendRedirect("login.jsp"); 
        return; 
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Vehículo - Sistema de Parqueo</title>
    <link rel="stylesheet" href="CSS/style.css"/>
    <style>
        /* Estilos modernos para ambiente empresarial */
        .edit-card {
            background: var(--bg-card, #1e293b);
            border: 1px solid rgba(255,255,255,.1);
            border-radius: 16px;
            padding: 32px 36px;
            width: 100%;
            max-width: 500px;
            margin: 40px auto;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
        }
        .form-group { margin-bottom: 18px; }
        .form-group label { display: block; font-size: .85rem; color: #94a3b8; margin-bottom: 6px; }
        .form-group input[type=text],
        .form-group select {
            width: 100%;
            background: rgba(255,255,255,.06);
            border: 1px solid rgba(255,255,255,.1);
            border-radius: 8px;
            color: #f8fafc;
            padding: 10px 14px;
            font-size: .95rem;
            transition: border-color 0.3s;
        }
        .form-group input[type=text]:focus,
        .form-group select:focus { outline: none; border-color: #38bdf8; }
        .form-group select option { background: #1e293b; color: #f8fafc; }
        
        .plate-badge {
            display: inline-block;
            background: #38bdf8;
            color: #0f172a;
            font-weight: 700;
            font-size: 1.1rem;
            padding: 6px 16px;
            border-radius: 6px;
            letter-spacing: 2px;
            margin-bottom: 25px;
            text-transform: uppercase;
        }
        .actions { display: flex; gap: 14px; margin-top: 25px; }
        .btn-save {
            background: #38bdf8;
            color: #0f172a;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            flex: 1;
        }
        .btn-cancel {
            background: transparent;
            color: #94a3b8;
            border: 1px solid rgba(255,255,255,.1);
            padding: 10px 20px;
            border-radius: 8px;
            text-decoration: none;
            text-align: center;
            flex: 1;
        }
        .btn-cancel:hover { background: rgba(255,255,255,.05); }
    </style>
</head>
<body>

<div id="titulo" style="text-align: center; margin-top: 30px;">
    <h2 style="color: #f8fafc;">&#x270F; Modificar Datos del Vehículo</h2>
</div>

<div class="edit-card">
    <div style="text-align: center;">
        <span style="display: block; font-size: 0.75rem; color: #94a3b8; margin-bottom: 5px;">PLACA DEL VEHÍCULO</span>
        <div class="plate-badge">${vehicle.plate}</div>
    </div>

    <form action="vehicles" method="post">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="plate" value="${vehicle.plate}"/>

        <div class="form-group">
            <label>Color</label>
            <input type="text" name="color" value="${vehicle.color}" required placeholder="Ej: Blanco"/>
        </div>

        <div class="form-group">
            <label>Marca</label>
            <input type="text" name="brand" value="${vehicle.brand}" required placeholder="Ej: Toyota"/>
        </div>

        <div class="form-group">
            <label>Modelo</label>
            <input type="text" name="model" value="${vehicle.model}" required placeholder="Ej: Hilux"/>
        </div>

        <div class="form-group">
            <label>Tipo de Vehículo</label>
            <select name="typeId" required>
                <c:forEach var="vt" items="${vehicleTypes}">
                    <option value="${vt.idVehicleType}" 
                        ${vt.idVehicleType == vehicle.idVehicleType ? 'selected' : ''}>
                        ${vt.description}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label>Dueño / Cliente Responsable</label>
            <select name="idCustomer">
                <option value="0">— Sin cliente asignado —</option>
                <c:forEach var="c" items="${customers}">
                    <option value="${c.id}" 
                        ${c.id == vehicle.idCustomer ? 'selected' : ''}>
                        ${c.name} (ID: ${c.id})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="actions">
            <button type="submit" class="btn-save">&#x2714; Guardar Cambios</button>
            <a href="vehicles" class="btn-cancel">Cancelar</a>
        </div>
    </form>
</div>

<script>
    // Pequeña validación antes de salir si hubo cambios
    function cancelar() {
        if(confirm("¿Desea salir sin guardar los cambios?")) {
            window.location.href = "vehicles";
        }
    }
</script>

</body>
</html>