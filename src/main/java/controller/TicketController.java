/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.RateDAO;
import model.dao.TicketDAO;
import model.entity.Rate;
import model.entity.Ticket;
import model.service.PaymentService;

/**
 *
 * @author Jefferson
 */
@WebServlet("/tickets")
public class TicketController extends HttpServlet {

    private TicketDAO ticketDAO = new TicketDAO();
    private RateDAO rateDAO = new RateDAO();
    private PaymentService paymentService = new PaymentService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("entrada".equals(action)) {
            try {
                int idCust = Integer.parseInt(request.getParameter("idCustomer"));
                String plate = request.getParameter("plate");

                //verificar id 
                int idGenerado = ticketDAO.openTicket(idCust, plate);

                if (idGenerado != -1) {
                    response.sendRedirect("list_tickets.jsp?success=1&ticketId=" + idGenerado);
                } else {
                    response.sendRedirect("list_tickets.jsp?error=1");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("list_tickets.jsp?error=invalid_data");
            }

        } else if ("salida".equals(action)) {
            try {
                String plate = request.getParameter("plate");
                
                //detalles del ticket activo por placa
                
                Ticket ticket = ticketDAO.getActiveTicketDetails(plate);
                
                if (ticket != null) {
                    //obtener la tarifa completa para el tipo de vehículo
                    Rate rate = rateDAO.getFullRateByVehicleType(ticket.getVehicle().getIdVehicleType());
                    
                    // Calcular el monto total
                    float totalCalculado = paymentService.calculateFinalAmount(
                        ticket.getEntryDate(), 
                        LocalDateTime.now(), 
                        rate, 
                        ticket.getCustomer().isDisabilityPresented()
                    );
                    
                    //cerrar ticket
                    if (ticketDAO.closeTicket(ticket.getId(), totalCalculado)) {
                        response.sendRedirect("list_tickets.jsp?exit=1&monto=" + totalCalculado);
                    } else {
                        response.sendRedirect("list_tickets.jsp?error=exit_failed");
                    }
                } else {
                    response.sendRedirect("list_tickets.jsp?error=not_found");
                }
            } catch (Exception e) {
                response.sendRedirect("list_tickets.jsp?error=processing_error");
            }
        }
    }
}
