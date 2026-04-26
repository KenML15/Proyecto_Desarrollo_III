<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <link rel="stylesheet" href="CSS/style.css">
    </head>
    <body>
        <br>
        <br>
        <br>
        <div class="container">
            <div class="header-container">
                <img src="IMG/logo.png" alt="Logo" class="logo">
                <h2 id="Titulo">Iniciar Sesión</h2>
            </div>
            <form action="login" method="post">
                <label>Usuario:</label>
                <input type="text" name="user" required>

                <label>Contraseña:</label>
                <input type="password" name="password" required>

                <c:if test="${not empty error}">
                    <p class="error-msg">${error}</p>
                </c:if>

                <div class="buttons">
                    <input type="submit" value="Entrar" class="save">
                </div>
            </form>
        </div>
    </body>
</html>
