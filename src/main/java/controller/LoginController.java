/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Jefferson
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String user = request.getParameter("user");
        String pass = request.getParameter("password");

        // Aquí puedes validar contra tu Base de Datos (UserDAO)
        // Por ahora, usemos una validación "quemada" para pruebas:
        if ("admin".equals(user) && "1234".equals(pass)) {
            
            // Creamos la sesión
            HttpSession session = request.getSession();
            session.setAttribute("username", user);
            
            // Redirigimos al menú principal
            response.sendRedirect("main_menu.html");
            
        } else {
            // Si falla, volvemos al login con un mensaje
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}