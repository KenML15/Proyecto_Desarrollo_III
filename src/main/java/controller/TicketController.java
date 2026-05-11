package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.AssignmentDAO;
import model.dao.RateDAO;
import model.dao.TicketDAO;
import model.entity.Rate;
import model.entity.Ticket;
import model.entity.VehicleAssignment;
import model.service.PaymentService;

@WebServlet("/tickets")
public class TicketController extends HttpServlet {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final RateDAO rateDAO = new RateDAO();
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final PaymentService paymentService = new PaymentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        // --- VER TIQUETE DE ENTRADA (después de asignar vehículo) ---
        if ("viewEntry".equals(action)) {
            try {
                int ticketId = Integer.parseInt(request.getParameter("ticketId"));

                // plate, lotId y slot son opcionales: el redirect desde AssignmentController
                // los incluye, pero se maneja defensivamente por si se llega por otra vía.
                String plate = request.getParameter("plate");

                String slotParam = request.getParameter("slot");
                int slot = (slotParam != null && !slotParam.isEmpty())
                        ? Integer.parseInt(slotParam) : 0;

                Ticket ticket = ticketDAO.findById(ticketId);

                // Fallback: buscar por placa si el ticketId no resolvió el ticket
                if (ticket == null && plate != null && !plate.isEmpty()) {
                    ticket = ticketDAO.getActiveTicketDetails(plate);
                }

                // Resolver placa desde el ticket si no vino en la URL
                if (plate == null || plate.isEmpty()) {
                    plate = (ticket != null && ticket.getVehicle() != null)
                            ? ticket.getVehicle().getPlate() : "";
                }

                VehicleAssignment assignment = (!plate.isEmpty())
                        ? assignmentDAO.findActiveAssignmentByPlate(plate) : null;
                String parkingName = assignment != null ? assignment.getLotName() : "Parqueo";

                // Si slot no vino en la URL, intentar obtenerlo de la asignación activa
                if (slot == 0 && assignment != null) {
                    slot = assignment.getAssignedSlot();
                }

                Rate rate = null;
                if (ticket != null && ticket.getVehicle() != null) {
                    rate = rateDAO.getFullRateByVehicleType(ticket.getVehicle().getIdVehicleType());
                }

                request.setAttribute("ticket", ticket);
                request.setAttribute("rate", rate);
                request.setAttribute("parkingName", parkingName);
                request.setAttribute("slotNumber", slot);
                request.setAttribute("entryOnly", true);
                request.getRequestDispatcher("ticket_entry_view.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("assignments?action=list");
            }
            return;
        }

        // --- VER TIQUETE DE UN VEHÍCULO ACTIVO (desde ocupación) ---
        if ("viewActive".equals(action)) {
            try {
                String plate = request.getParameter("plate");
                Ticket ticket = ticketDAO.getActiveTicketDetails(plate);
                VehicleAssignment assignment = assignmentDAO.findActiveAssignmentByPlate(plate);

                Rate rate = null;
                if (ticket != null && ticket.getVehicle() != null) {
                    rate = rateDAO.getFullRateByVehicleType(ticket.getVehicle().getIdVehicleType());
                }

                float amount = 0;
                if (ticket != null && rate != null) {
                    amount = paymentService.calculateFinalAmount(
                        ticket.getEntryDate(),
                        java.time.LocalDateTime.now(),
                        rate,
                        ticket.getCustomer() != null && ticket.getCustomer().isDisabilityPresented()
                    );
                }

                request.setAttribute("ticket", ticket);
                request.setAttribute("rate", rate);
                request.setAttribute("totalAmount", amount);
                request.setAttribute("parkingName", assignment != null ? assignment.getLotName() : "Parqueo");
                request.setAttribute("slotNumber", assignment != null ? assignment.getAssignedSlot() : 0);
                request.setAttribute("entryOnly", false);
                request.getRequestDispatcher("ticket_entry_view.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("assignments?action=list");
            }
            return;
        }

        List<Ticket> tickets = ticketDAO.findAll();
        request.setAttribute("tickets", tickets);
        request.getRequestDispatcher("list_tickets.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String role = (String) request.getSession().getAttribute("role");

        //entrada
        if ("entrada".equals(action) && ("admin".equals(role) || "clerk".equals(role))) {

            int idCustomer = Integer.parseInt(request.getParameter("idCustomer").trim());
            String plate = request.getParameter("plate").trim().toUpperCase();

            VehicleAssignment assignment = assignmentDAO.findActiveAssignmentByPlate(plate);
            if (assignment == null) {
                response.sendRedirect("tickets?error=not_assigned");
                return;
            }

            if (ticketDAO.findActiveTicketByPlate(plate) != -1) {
                response.sendRedirect("tickets?error=already_open");
                return;
            }

            int newId = ticketDAO.openTicket(idCustomer, plate);
            if (newId > 0) {
                response.sendRedirect("tickets?success=1&ticketId=" + newId);
            } else {
                response.sendRedirect("tickets?error=1");
            }

        //salirda
        } else if ("salida".equals(action) && ("admin".equals(role) || "clerk".equals(role))) {

            String plate = request.getParameter("plate").trim().toUpperCase();

            Ticket ticket = ticketDAO.getActiveTicketDetails(plate);
            if (ticket == null) {
                response.sendRedirect("tickets?error=not_found");
                return;
            }

            Rate rate = rateDAO.getFullRateByVehicleType(ticket.getVehicle().getIdVehicleType());
            if (rate == null) {
                response.sendRedirect("tickets?error=processing_error");
                return;
            }

            VehicleAssignment assignment = assignmentDAO.findActiveAssignmentByPlate(plate);
            String parkingName = assignment != null ? assignment.getLotName() : "Parqueo";
            int slotNumber = assignment != null ? assignment.getAssignedSlot() : 0;

            float amount = paymentService.calculateFinalAmount(
                    ticket.getEntryDate(),
                    java.time.LocalDateTime.now(),
                    rate,
                    ticket.getCustomer().isDisabilityPresented()
            );

            request.setAttribute("ticket", ticket);
            request.setAttribute("rate", rate);
            request.setAttribute("totalAmount", amount);
            request.setAttribute("parkingName", parkingName);
            request.setAttribute("slotNumber", slotNumber);
            request.getRequestDispatcher("ticket_chekout.jsp").forward(request, response);

        //pago
        } else if ("confirmarPago".equals(action) && ("admin".equals(role) || "clerk".equals(role))) {

            int idTicket = Integer.parseInt(request.getParameter("idTicket").trim());
            float monto = Float.parseFloat(request.getParameter("monto").trim());
            String plate = request.getParameter("plate").trim().toUpperCase();

            boolean closed = ticketDAO.closeTicket(idTicket, monto);
            if (!closed) {
                response.sendRedirect("tickets?error=exit_failed");
                return;
            }

            assignmentDAO.deactivateAssignmentByPlate(plate);

            Ticket closedTicket = ticketDAO.findById(idTicket);
            VehicleAssignment assignment = assignmentDAO.findLastAssignmentByPlate(plate);
            Rate rate = rateDAO.getFullRateByVehicleType(closedTicket.getVehicle().getIdVehicleType());

            request.setAttribute("ticket", closedTicket);
            request.setAttribute("rate", rate);
            request.setAttribute("totalAmount", monto);
            request.setAttribute("parkingName", assignment != null ? assignment.getLotName() : "Parqueo");
            request.setAttribute("slotNumber", assignment != null ? assignment.getAssignedSlot() : 0);
            request.getRequestDispatcher("ticket_receipt.jsp").forward(request, response);

        } else {
            response.sendRedirect("tickets");
        }
    }
}
