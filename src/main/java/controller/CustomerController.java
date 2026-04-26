/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.CustomerDAO;
import model.entity.Customer;

/**
 * @author Jefferson
 */
@WebServlet("/customers")
public class CustomerController extends HttpServlet {

    private CustomerDAO customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action     = request.getParameter("action");
        String name       = request.getParameter("name");
        boolean disability = request.getParameter("discapacity") != null;
        String cedula     = request.getParameter("cedula");
        String telefono   = request.getParameter("telefono");
        String correo     = request.getParameter("correo");

        if ("update".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Customer customer = new Customer(id, name, disability);
            customer.setCedula(cedula);
            customer.setTelefono(telefono);
            customer.setCorreo(correo);
            customerDAO.update(customer);
        } else {
            Customer customer = new Customer(0, name, disability);
            customer.setCedula(cedula);
            customer.setTelefono(telefono);
            customer.setCorreo(correo);
            customerDAO.insert(customer);
        }

        response.sendRedirect("customers");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            int customerId = Integer.parseInt(request.getParameter("id"));
            customerDAO.delete(customerId);
            response.sendRedirect("customers");

        } else if ("edit".equalsIgnoreCase(action)) {
            int customerId = Integer.parseInt(request.getParameter("id"));
            Customer customer = customerDAO.findById(customerId);
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("edit_customer.jsp").forward(request, response);

        } else {
            List<Customer> customers = customerDAO.findAll();
            request.setAttribute("customers", customers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("show_all_customers.jsp");
            dispatcher.forward(request, response);
        }
    }
}