package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.dao.UserDAO;

/**
 * Controlador de Login con soporte de roles (admin / clerk).
 * Redirige a menu_admin.jsp o menu_clerk.jsp según el rol del usuario.
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si ya hay sesión activa, redirigir directo al menú
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            redirectByRole((String) session.getAttribute("role"), response);
            return;
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("user");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();
        String role = userDAO.authenticate(username, password);  // "admin", "clerk" o null

        if (role != null) {
            // Credenciales válidas → crear sesión con usuario y rol
            HttpSession session = request.getSession();
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            // Redirigir al menú correspondiente
            redirectByRole(role, response);

        } else {
            // Credenciales incorrectas
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /** Redirige al menú según el rol. */
    private void redirectByRole(String role, HttpServletResponse response) throws IOException {
        if ("admin".equals(role)) {
            response.sendRedirect("menu_admin.jsp");
        } else {
            response.sendRedirect("menu_clerk.jsp");
        }
    }
}