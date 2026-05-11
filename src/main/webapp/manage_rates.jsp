<%-- 
    Document   : manage_rates
    Created on : May 10, 2026, 10:36:15 PM
    Author     : Jefferson
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Gestión de Tarifas</title>
    <link rel="stylesheet" href="CSS/estilos.css">
</head>
<body>
    <div class="header-container">
        <div id="titulo"><h2>Configuración de Tarifas</h2></div>
        <img src="IMG/logo.png" class="logo">
    </div>

    <div class="container">
        <h2>Defina los montos para el tipo de vehículo</h2>
        
        <form action="rates" method="post">
            <input type="hidden" name="idVehicleType" value="${idVehicleType}">

            <label>Tarifa Media Hora (₡)</label>
            <input type="number" step="0.01" name="halfHour" value="${rate != null ? rate.halfHour : 0}" required>

            <label>Tarifa Hora Completa (₡)</label>
            <input type="number" step="0.01" name="hour" value="${rate != null ? rate.hour : 0}" required>

            <label>Tarifa por Día (₡)</label>
            <input type="number" step="0.01" name="day" value="${rate != null ? rate.day : 0}" required>

            <label>Tarifa por Semana (₡)</label>
            <input type="number" step="0.01" name="week" value="${rate != null ? rate.week : 0}" required>

            <label>Tarifa Mensual (₡)</label>
            <input type="number" step="0.01" name="month" value="${rate != null ? rate.month : 0}" required>

            <label>Tarifa Anual (₡)</label>
            <input type="number" step="0.01" name="year" value="${rate != null ? rate.year : 0}" required>

            <div class="buttons">
                <button type="submit" class="save">Guardar Cambios</button>
                <a href="vehicleTypes" class="cancel-link">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>