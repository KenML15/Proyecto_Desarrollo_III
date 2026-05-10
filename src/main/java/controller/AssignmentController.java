package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.VehicleDAO;
import model.dao.ParkingLotDAO;
import model.dao.AssignmentDAO;
import model.entity.Vehicle;
import model.entity.ParkingLot;
import model.entity.ParkingSpace;
import model.entity.VehicleAssignment;

@WebServlet("/assignments")
public class AssignmentController extends HttpServlet {

    private VehicleDAO vehicleDAO = new VehicleDAO();
    private ParkingLotDAO parkingDAO = new ParkingLotDAO();
    private AssignmentDAO assignmentDAO = new AssignmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // --- 1. PREPARAR ASIGNACIÓN (Vista) ---
        if ("prepare".equalsIgnoreCase(action)) {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            List<ParkingLot> lots = parkingDAO.findAll();
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("parkingLots", lots);
            request.getRequestDispatcher("assign_vehicle.jsp").forward(request, response);
            return;
        }

        // --- 2. LISTAR OCUPACIÓN (Vista) ---
        if ("list".equalsIgnoreCase(action)) {
            String lotIdStr = request.getParameter("lotId");
            List<VehicleAssignment> activeList;
            if (lotIdStr != null && !lotIdStr.isEmpty()) {
                try {
                    int lotId = Integer.parseInt(lotIdStr);
                    activeList = assignmentDAO.findActiveAssignmentsByLot(lotId);
                    request.setAttribute("lotId", lotId);
                } catch (NumberFormatException e) {
                    activeList = assignmentDAO.findActiveAssignments();
                }
            } else {
                activeList = assignmentDAO.findActiveAssignments();
            }
            request.setAttribute("activeAssignments", activeList);
            request.getRequestDispatcher("show_occupancy.jsp").forward(request, response);
            return;
        }

        // --- 3. LIBERAR VEHÍCULO (Acción desde enlace) ---
        if ("release".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                assignmentDAO.releaseVehicle(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("assignments?action=list&nocache=" + System.currentTimeMillis());
            return;
        }

        // --- 4. DASHBOARD (Vista) ---
        if ("dashboard".equalsIgnoreCase(action)) {
            List<ParkingLot> report = assignmentDAO.getOccupancyReport();
            request.setAttribute("report", report);
            request.getRequestDispatcher("parking_dashboard.jsp").forward(request, response);
            return;
        }


        if ("board".equalsIgnoreCase(action)) {
            try {
                int lotId = Integer.parseInt(request.getParameter("id"));
                List<ParkingSpace> board = assignmentDAO.getBoardByParkingLot(lotId);
                request.setAttribute("spaces", board);
                request.setAttribute("lotName", request.getParameter("name"));
                request.getRequestDispatcher("parking_board_view.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendRedirect("assignments?action=dashboard");
            }
            return;
        }

        if ("manageSlots".equalsIgnoreCase(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    int lotId = Integer.parseInt(idStr);
                    List<ParkingSpace> currentSlots = assignmentDAO.getBoardByParkingLot(lotId);
                    request.setAttribute("lotId", lotId);
                    request.setAttribute("currentSlots", currentSlots);
                    request.getRequestDispatcher("manage_slots.jsp").forward(request, response);
                } catch (Exception e) {
                    response.sendRedirect("assignments?action=dashboard");
                }
                return;
            }
        }


        response.sendRedirect("main_menu.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("saveSlot".equalsIgnoreCase(action)) {
            try {
                int idLot = Integer.parseInt(request.getParameter("idParkingLot"));
                int slotNumber = Integer.parseInt(request.getParameter("slotNumber"));
                boolean isDisability = request.getParameter("isDisability") != null;

                parkingDAO.saveOrUpdateSlot(idLot, slotNumber, isDisability);
                

                response.sendRedirect("assignments?action=manageSlots&id=" + idLot);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("assignments?action=dashboard&error=save");
            }
            return;
        }

        String plate = request.getParameter("plateVehicle");
        String lotIdStr = request.getParameter("idParkingLot");
        String slotStr = request.getParameter("assignedSlot");

        if (plate != null && lotIdStr != null && slotStr != null) {
            try {
                int idLot = Integer.parseInt(lotIdStr);
                int slotNumber = Integer.parseInt(slotStr);

                if (assignmentDAO.hasCapacity(idLot)) {
                    boolean success = assignmentDAO.insert(plate, idLot, slotNumber);
                    if (success) {
                        response.sendRedirect("main_menu.html?msg=success");
                    } else {
                        response.sendRedirect("assignments?action=prepare&error=db");
                    }
                } else {
                    response.sendRedirect("assignments?action=prepare&error=full");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("assignments?action=prepare&error=data");
            }
            return;
        }

        response.sendRedirect("main_menu.html");
    }
}
