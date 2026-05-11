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
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    int id = Integer.parseInt(idStr);
                    assignmentDAO.releaseVehicle(id);
                    // Redirigir con un timestamp para forzar al navegador a recargar la tabla
                    response.sendRedirect("assignments?action=list&msg=success&t=" + System.currentTimeMillis());
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("assignments?action=list&error=db");
            }
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

                    // --- NUEVO: Obtener el objeto ParkingLot completo ---
                    ParkingLot lot = parkingDAO.findById(lotId);
                    List<ParkingSpace> currentSlots = assignmentDAO.getBoardByParkingLot(lotId);

                    request.setAttribute("lot", lot); // Pasamos el objeto con su capacidad
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

        // 1. Guardar o actualizar espacios (Slots)
        if ("saveSlot".equalsIgnoreCase(action)) {
    try {
        int idLot = Integer.parseInt(request.getParameter("idParkingLot"));
        int slotNumber = Integer.parseInt(request.getParameter("slotNumber"));
        boolean isDisability = request.getParameter("isDisability") != null;

        // --- VALIDACIÓN DE SEGURIDAD ---
        ParkingLot lot = parkingDAO.findById(idLot);
        if (slotNumber > lot.getNumberOfSpaces()) {
            // Si el espacio supera la capacidad física, devolvemos error
            response.sendRedirect("assignments?action=manageSlots&id=" + idLot + "&error=limit_exceeded");
            return;
        }

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

                // 1. Validar límite físico (Regla #4)
                ParkingLot lot = parkingDAO.findById(idLot);
                if (slotNumber > lot.getNumberOfSpaces()) {
                    response.sendRedirect("assignments?action=prepare&error=slot_out_of_range&max=" + lot.getNumberOfSpaces() + "&slot=" + slotNumber);
                    return;
                }

                // --- [NUEVO] REGLA DE NEGOCIO #5: Validar Espacio de Discapacidad ---
                // Primero obtenemos la información de ese espacio específico
                ParkingSpace space = parkingDAO.getSlotInfo(idLot, slotNumber);

                // Si el espacio está marcado como exclusivo para discapacidad
                if (space != null && space.isDisability()) {
                    // Verificamos si el vehículo tiene al menos un dueño discapacitado
                    if (!vehicleDAO.hasDisabilityOwner(plate)) {
                        response.sendRedirect("assignments?action=prepare&error=disability_required&plate=" + plate + "&slot=" + slotNumber);
                        return;
                    }
                }

                // 2. Validar si ya está parqueado (Regla #1 y #2)
                if (assignmentDAO.isVehicleAlreadyParked(plate)) {
                    response.sendRedirect("assignments?action=prepare&error=already_parked&plate=" + plate);
                    return;
                }

                // 3. Validar capacidad general e insertar
                if (assignmentDAO.hasCapacity(idLot)) {
                    boolean success = assignmentDAO.insert(plate, idLot, slotNumber);
                    if (success) {
                        response.sendRedirect("menu_admin.jsp?msg=success");
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
    }
}
