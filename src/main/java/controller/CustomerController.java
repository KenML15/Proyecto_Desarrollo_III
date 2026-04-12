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
 *
 * @author Jefferson
 */

/**
 *Gestión de Clientes
 */

@WebServlet("/customers")
public class CustomerController extends HttpServlet{
    
    private CustomerDAO customerDAO;

    public CustomerController() {
        customerDAO = new CustomerDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //seteamos carácteres especiales (ñ, á, é...)
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        
        //capturar formulario
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        
        //verificar discapacidad
        boolean disability = request.getParameter("discapacity") != null;

        Customer customer = new Customer(id, name, disability);
        if ("update".equalsIgnoreCase(action)) {
            customerDAO.update(customer); // Ahora sí ejecuta la actualización
        } else {
            customerDAO.insert(customer);
        }

        //lista de clientes
        response.sendRedirect("customers"); 
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        //eliminar cliente
        if ("delete".equalsIgnoreCase(action)) {
            int customerId = Integer.parseInt(request.getParameter("id"));
            customerDAO.delete(customerId); 
            response.sendRedirect("customers");
            
        //editar cliente (
        } else if ("edit".equalsIgnoreCase(action)) {
            int customerId = Integer.parseInt(request.getParameter("id"));
            Customer customer = customerDAO.findById(customerId); 
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("edit_customer.jsp").forward(request, response);
            
        //listar todos los clientes
        } else {
            List<Customer> customers = customerDAO.findAll(); 
            request.setAttribute("customers", customers);
            RequestDispatcher dispatcher = request.getRequestDispatcher("show_all_customers.jsp");
            dispatcher.forward(request, response);
        }
    }
}
