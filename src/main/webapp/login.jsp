<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="CSS/style.css">
</head>
<body>
    <div class="container">
        <h2>Iniciar Sesión</h2>
        <form action="login" method="post">
            <label>Usuario:</label>
            <input type="text" name="user" required>
            
            <label>Contraseña:</label>
            <input type="password" name="password" required>
            
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>

            <div class="buttons">
                <input type="submit" value="Entrar" class="save">
            </div>
        </form>
    </div>
</body>
</html>