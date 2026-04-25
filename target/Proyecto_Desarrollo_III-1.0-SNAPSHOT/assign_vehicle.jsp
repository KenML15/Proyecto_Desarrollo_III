<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="CSS/style.css">
    <title>Asignar Vehículo a Parqueo</title>
</head>
<body>
    <div class="container">
        <div id="titulo">
            <h2>Registrar Ingreso de Vehículo</h2>
        </div>
        
        <form action="assignments" method="POST" class="form">
            <label>SELECCIONAR VEHÍCULO (PLACA):</label>
            <select name="plateVehicle" required>
                <option value="">-- Seleccione un vehículo --</option>
                <c:forEach var="v" items="${vehicles}">
                    <option value="${v.plate}">${v.plate} - ${v.brand} ${v.model}</option>
                </c:forEach>
            </select>

            <label>SELECCIONAR PARQUEO / LOTE:</label>
            <select name="idParkingLot" required>
                <option value="">-- Seleccione el área de destino --</option>
                <c:forEach var="p" items="${parkingLots}">
                    <option value="${p.id}">${p.name} (Capacidad: ${p.numberOfSpaces})</option>
                </c:forEach>
            </select>

            <div class="botones">
                <button type="submit" class="save">Registrar Ingreso</button>
                <a href="main_menu.html" class="cancel">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>