package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.AssignmentDAO;
import model.dao.ParkingLotDAO;
import model.dao.TicketDAO;
import model.dao.VehicleDAO;
import model.entity.ParkingLot;
import model.entity.ParkingSpace;
import model.entity.Vehicle;
import model.entity.VehicleAssignment;

@WebServlet("/assignments")
public class AssignmentController extends HttpServlet {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final ParkingLotDAO parkingDAO = new ParkingLotDAO();
    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final TicketDAO ticketDAO = new TicketDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("prepare".equalsIgnoreCase(action)) {
            request.setAttribute("vehicles", vehicleDAO.findAll());
            request.setAttribute("parkingLots", parkingDAO.findAll());
            request.getRequestDispatcher("assign_vehicle.jsp").forward(request, response);
            return;
        }

        if ("list".equalsIgnoreCase(action)) {
            String lotIdStr = request.getParameter("lotId");
            List<VehicleAssignment> activeList;
            if (lotIdStr != null && !lotIdStr.isEmpty()) {
                int filterLotId = Integer.parseInt(lotIdStr);
                activeList = new java.util.ArrayList<>();
                for (VehicleAssignment va : assignmentDAO.findActiveAssignments()) {
                    if (va.getIdParkingLot() == filterLotId) {
                        activeList.add(va);
                    }
                }
                request.setAttribute("lotId", lotIdStr);
            } else {
                activeList = assignmentDAO.findActiveAssignments();
            }
            request.setAttribute("activeAssignments", activeList);
            request.getRequestDispatcher("show_occupancy.jsp").forward(request, response);
            return;
        }

        if ("release".equalsIgnoreCase(action)) {
            assignmentDAO.releaseVehicle(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("assignments?action=list&msg=success");
            return;
        }

        if ("dashboard".equalsIgnoreCase(action)) {
            request.setAttribute("report", assignmentDAO.getOccupancyReport());
            request.getRequestDispatcher("parking_dashboard.jsp").forward(request, response);
            return;
        }

        if ("board".equalsIgnoreCase(action)) {
            int lotId = Integer.parseInt(request.getParameter("id"));
            String lotName = request.getParameter("name");
            request.setAttribute("lot", parkingDAO.findById(lotId));
            request.setAttribute("boardSpaces", assignmentDAO.getBoardByParkingLot(lotId));
            request.setAttribute("lotName", lotName);
            request.getRequestDispatcher("parking_board_view.jsp").forward(request, response);
            return;
        }

        if ("manageSlots".equalsIgnoreCase(action)) {
            int lotId = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("lot", parkingDAO.findById(lotId));
            request.setAttribute("lotId", lotId);
            request.setAttribute("currentSlots", assignmentDAO.getBoardByParkingLot(lotId));
            request.getRequestDispatcher("manage_slots.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("menu_admin.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("saveSlot".equalsIgnoreCase(action)) {
            int idLot = Integer.parseInt(request.getParameter("idParkingLot"));
            int slotNumber = Integer.parseInt(request.getParameter("slotNumber"));
            boolean isDisability = request.getParameter("isDisability") != null;

            ParkingLot lot = parkingDAO.findById(idLot);
            if (slotNumber > lot.getNumberOfSpaces()) {
                response.sendRedirect("assignments?action=manageSlots&id=" + idLot + "&error=limit_exceeded");
                return;
            }

            parkingDAO.saveOrUpdateSlot(idLot, slotNumber, isDisability);
            response.sendRedirect("assignments?action=manageSlots&id=" + idLot);
            return;
        }

        String plate = request.getParameter("plateVehicle");
        int idLot = Integer.parseInt(request.getParameter("idParkingLot"));
        int slotNumber = Integer.parseInt(request.getParameter("assignedSlot"));

        try {
            ParkingLot lot = parkingDAO.findById(idLot);

            if (slotNumber > lot.getNumberOfSpaces()) {
                response.sendRedirect("assignments?action=prepare&error=slot_out_of_range");
                return;
            }

            ParkingSpace space = parkingDAO.getSlotInfo(idLot, slotNumber);
            if (space != null && space.isDisability() && !vehicleDAO.hasDisabilityOwner(plate)) {
                response.sendRedirect("assignments?action=prepare&error=disability_required");
                return;
            }

            if (assignmentDAO.isVehicleAlreadyParked(plate)) {
                response.sendRedirect("assignments?action=prepare&error=already_parked");
                return;
            }

            if (!assignmentDAO.hasCapacity(idLot)) {
                response.sendRedirect("assignments?action=prepare&error=full");
                return;
            }

            if (assignmentDAO.insert(plate, idLot, slotNumber)) {
                Vehicle vehicle = vehicleDAO.findByPlate(plate);
                int idCustomer = (vehicle != null) ? vehicle.getIdCustomer() : 0;

                if (idCustomer > 0 && ticketDAO.findActiveTicketByPlate(plate) == -1) {
                    int newTicketId = ticketDAO.openTicket(idCustomer, plate);
                    response.sendRedirect("tickets?action=viewEntry&ticketId=" + newTicketId
                            + "&plate=" + java.net.URLEncoder.encode(plate, "UTF-8")
                            + "&lotId=" + idLot
                            + "&slot=" + slotNumber);
                } else {
                    response.sendRedirect("assignments?action=list&msg=assigned");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("assignments?action=prepare&error=system");
        }
    }
}