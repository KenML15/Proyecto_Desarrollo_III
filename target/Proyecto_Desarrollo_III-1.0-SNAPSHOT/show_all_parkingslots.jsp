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

        <div class="container" style="max-width: 900px;"> <table border="1" id="table" style="width: 100%; border-collapse: collapse;">
                <tr id="encabezado">
                    <th>ID</th>
                    <th>Nombre del Local</th>
                    <th>Capacidad (Espacios)</th>
                    <th>Acciones</th> </tr>
                <c:forEach var="p" items="${listaParqueos}">
                    <tr>
                        <td><c:out value="${p.id}" /></td>
                        <td><c:out value="${p.name}" /></td>
                        <td><c:out value="${p.numberOfSpaces}" /></td>
                        <td>
                            <a href="parkingLot?action=edit&id=${p.id}" class="save" 
                               style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;">
                               Editar
                            </a>
                            
                            <a href="parkingLot?action=delete&id=${p.id}" class="cancel" 
                               style="padding: 5px 10px; text-decoration: none; font-size: 0.8rem;"
                               onclick="return confirm('¿Seguro que desea eliminar el parqueo: ${p.name}?')">
                               Eliminar
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <div style="margin-top: 20px;">
            <a href="main_menu.html" class="cancel" style="text-decoration: none; padding: 10px 20px;">Volver al menú</a>
        </div>
    </body>
</html>