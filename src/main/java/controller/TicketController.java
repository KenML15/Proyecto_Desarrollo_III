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
import model.dao.TicketDAO;

/**
 *
 * @author Jefferson
 */
@WebServlet("/tickets")
public class TicketController extends HttpServlet {

    private TicketDAO ticketDAO = new TicketDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("entrada".equals(action)) {
            try {
                int idCust = Integer.parseInt(request.getParameter("idCustomer"));
                String plate = request.getParameter("plate");
                
                //verificar id 
                int idGenerado = ticketDAO.openTicket(idCust, plate);
                
                if(idGenerado != -1) {
                    response.sendRedirect("list_tickets.jsp?success=1&ticketId=" + idGenerado);
                } else {
                    response.sendRedirect("list_tickets.jsp?error=1");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("list_tickets.jsp?error=invalid_data");
            }

        } else if ("salida".equals(action)) {
            int idTicket = Integer.parseInt(request.getParameter("idTicket"));
            float total = Float.parseFloat(request.getParameter("total"));
            
            if(ticketDAO.closeTicket(idTicket, total)) {
                response.sendRedirect("list_tickets.jsp?exit=1");
            } else {
                response.sendRedirect("list_tickets.jsp?error=exit_failed");
            }
        }
    }
}
