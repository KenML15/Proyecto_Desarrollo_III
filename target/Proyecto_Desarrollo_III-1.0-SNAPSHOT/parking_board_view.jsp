<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Tablero de Espacios</title>
    <link rel="stylesheet" href="CSS/style.css">
    <style>
        .board-grid {
            display: flex;
            flex-wrap: wrap;
            gap: 12px;
            padding: 10px 0;
        }
        .space-cell {
            width: 90px;
            min-height: 80px;
            border-radius: 10px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            font-size: .8rem;
            font-weight: 600;
            border: 2px solid transparent;
            padding: 6px;
            text-align: center;
            word-break: break-all;
        }
        .space-free     { background: rgba(46,204,113,.15); border-color: #2ecc71; color: #2ecc71; }
        .space-occupied { background: rgba(231,76,60,.15);  border-color: #e74c3c; color: #e74c3c; }
        .space-disability { border-style: dashed; }
        .space-num { font-size: 1rem; font-weight: 700; margin-bottom: 4px; }
        .legend { display: flex; gap: 20px; margin-bottom: 20px; font-size: .85rem; align-items: center; }
        .legend-dot { width: 14px; height: 14px; border-radius: 3px; display: inline-block; margin-right: 6px; }
        .dot-free     { background: #2ecc71; }
        .dot-occupied { background: #e74c3c; }
        .empty-msg { color: var(--text-muted); padding: 30px; text-align: center; }
    </style>
</head>
<body>

<div id="titulo">
    <h2>Tablero — ${not empty lotName ? lotName : lot.name}</h2>
</div>

<div class="container container--wide">

    <div class="legend">
        <span><span class="legend-dot dot-free"></span>Libre</span>
        <span><span class="legend-dot dot-occupied"></span>Ocupado</span>
        <span style="color:var(--text-muted); font-size:.8rem;">Borde discontinuo = discapacidad</span>
    </div>

    <c:choose>
        <c:when test="${not empty boardSpaces}">
            <div class="board-grid">
                <c:forEach var="s" items="${boardSpaces}">
                    <div class="space-cell
                        ${s.occupied ? 'space-occupied' : 'space-free'}
                        ${s.disability ? 'space-disability' : ''}">
                        <span class="space-num">#${s.number}</span>
                        <c:choose>
                            <c:when test="${s.occupied}"><span>${s.plate}</span></c:when>
                            <c:when test="${s.disability}"><span>Disc.</span></c:when>
                            <c:otherwise><span>Libre</span></c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <p class="empty-msg">Este parqueo no tiene espacios configurados todavía.<br>
                <a href="assignments?action=manageSlots&id=${lot.id}" class="save btn-table" style="margin-top:12px; display:inline-block;">Configurar Espacios</a>
            </p>
        </c:otherwise>
    </c:choose>
</div>

<div class="footer-nav">
    <a href="assignments?action=dashboard" class="cancel">← Volver al Panel</a>
</div>

</body>
</html>
