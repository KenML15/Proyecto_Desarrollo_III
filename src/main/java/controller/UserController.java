package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.UserDAO;
import model.entity.User;

@WebServlet("/users")
public class UserController extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            userDAO.delete(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("users");

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("user", userDAO.findById(id));
            request.getRequestDispatcher("edit_user.jsp").forward(request, response);

        } else if ("add".equalsIgnoreCase(action)) {
            request.getRequestDispatcher("insert_user.jsp").forward(request, response);

        } else {
            List<User> users = userDAO.findAll();
            request.setAttribute("users", users);
            request.getRequestDispatcher("show_all_users.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("update".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int active = request.getParameter("active") != null ? 1 : 0;

            if (userDAO.usernameExists(username, id)) {
                request.setAttribute("error", "El nombre de usuario ya está en uso.");
                request.setAttribute("user", userDAO.findById(id));
                request.getRequestDispatcher("edit_user.jsp").forward(request, response);
                return;
            }

            userDAO.update(new User(id, username, password, role, active));
            response.sendRedirect("users?msg=updated");

        } else {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            int active = request.getParameter("active") != null ? 1 : 0;

            if (userDAO.usernameExists(username, 0)) {
                request.setAttribute("error", "El nombre de usuario ya está en uso.");
                request.getRequestDispatcher("insert_user.jsp").forward(request, response);
                return;
            }

            boolean ok = userDAO.insert(new User(0, username, password, role, active));
            response.sendRedirect(ok ? "users?msg=created" : "users?error=db");
        }
    }
}