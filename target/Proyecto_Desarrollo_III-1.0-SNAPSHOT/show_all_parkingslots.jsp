<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="CSS/style.css">
        <title>Gestión de Parqueos</title>
    </head>
    <body>
        <div id="titulo">
            <h2>Gestión de Locales de Parqueo</h2>
        </div>

        <div class="container container--wide">
            <table border="1" id="table">
                <tr id="encabezado">
                    <th>ID</th>
                    <th>Nombre del Local</th>
                    <th>Capacidad (Espacios)</th>
                    <th>Acciones</th>
                </tr>
                <c:forEach var="p" items="${listaParqueos}">
                    <tr>
                        <td><c:out value="${p.id}" /></td>
                        <td><c:out value="${p.name}" /></td>
                        <td><c:out value="${p.numberOfSpaces}" /></td>
                        <td>
                            <a href="parkingLot?action=edit&id=${p.id}" class="save btn-table">
                               Editar
                            </a>

                            <a href="parkingLot?action=delete&id=${p.id}" class="cancel btn-table"
                               onclick="return confirm('¿Seguro que desea eliminar el parqueo: ${p.name}?')">
                               Eliminar
                            </a>

                            <a href="assignments?action=list&lotId=${p.id}" class="save btn-table btn-table--info">
                               Ver Ocupación
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div class="footer-nav">
            <a href="main_menu.html" class="cancel">Volver al menú</a>
        </div>
    </body>
</html>
