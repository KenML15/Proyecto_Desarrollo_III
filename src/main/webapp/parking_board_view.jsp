<%-- 
    Document   : parking_board_view
    Created on : 9 may 2026, 6:03:35?p.m.
    Author     : Kenneth
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="CSS/style.css">
    <title>Tablero de Control</title>
    <style>
        .grid-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
            gap: 15px;
            padding: 20px;
        }
        .space-card {
            height: 100px;
            border-radius: 8px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            color: white;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .occupied { background-color: #e74c3c; } /* Rojo */
        .available { background-color: #2ecc71; } /* Verde */
        .plate-label { font-size: 0.8rem; margin-top: 5px; background: rgba(0,0,0,0.2); padding: 2px 5px; border-radius: 3px; }
    </style>
</head>
<body>
    <div id="titulo">
        <h2>Tablero: ${lotName}</h2>
    </div>

    <div class="container">
        <div class="grid-container">
         <c:forEach var="s" items="${spaces}">
    <%-- Lógica de clases CSS --%>
    <c:set var="cardClass" value="" />
    <c:choose>
    <%-- 1. PRIORIDAD: ¿Está ocupado? (ROJO) --%>
    <c:when test="${s.occupied}">
        <c:set var="cardClass" value="occupied" />
        <c:set var="statusText" value="${s.plate}" />
    </c:when>
    
    <%-- 2. ¿Es de discapacidad y está LIBRE? (AZUL) --%>
    <c:when test="${s.disability}">
        <c:set var="cardClass" value="available-disability" />
        <c:set var="statusText" value="DISCAPACIDAD" />
    </c:when>
    
    <%-- 3. Caso por defecto: LIBRE REGULAR (VERDE) --%>
    <c:otherwise>
        <c:set var="cardClass" value="available" />
        <c:set var="statusText" value="LIBRE" />
    </c:otherwise>
</c:choose>

    <div class="space-card ${cardClass}">
        <span>#${s.number}</span>
        <c:if test="${s.disability && !s.occupied}">
            <span style="font-size: 20px;">?</span>
        </c:if>
        <div class="plate-label">
            <c:out value="${s.occupied ? s.plate : 'LIBRE'}" />
        </div>
    </div>
</c:forEach>
        </div>
        
        <div style="margin-top: 20px; text-align: center;">
            <a href="assignments?action=dashboard" class="cancel">Volver al Reporte General</a>
        </div>
    </div>
</body>
</html>
