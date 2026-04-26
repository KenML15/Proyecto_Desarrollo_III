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
import model.dao.AssignmentDAO; // Importamos el nuevo DAO
import model.entity.Vehicle;
import model.entity.ParkingLot;
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

        if ("prepare".equalsIgnoreCase(action)) {
            List<Vehicle> vehicles = vehicleDAO.findAll();
            List<ParkingLot> lots = parkingDAO.findAll();
            request.setAttribute("vehicles", vehicles);
            request.setAttribute("parkingLots", lots);

            request.getRequestDispatcher("assign_vehicle.jsp").forward(request, response);
            return; // <--- CRUCIAL: Detiene la ejecución aquí
        }

        if ("list".equalsIgnoreCase(action)) {
            String lotIdStr = request.getParameter("lotId");
            List<VehicleAssignment> activeList;

            if (lotIdStr != null && !lotIdStr.isEmpty()) {
                try {
                    int lotId = Integer.parseInt(lotIdStr);
                    activeList = assignmentDAO.findActiveAssignmentsByLot(lotId);
                    // Pasamos el lotId a la vista para personalizar el título
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
        
        if ("release".equalsIgnoreCase(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = assignmentDAO.releaseVehicle(id);

                if (ok) {
                    System.out.println(">>> CONTROLADOR: Salida exitosa del ID " + id);
                }
            } catch (NumberFormatException e) {
                System.err.println(">>> CONTROLADOR ERROR: ID no válido");
            }

            // Forzamos la limpieza de caché antes de redireccionar
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.sendRedirect("assignments?action=list&nocache=" + System.currentTimeMillis());
            return;
        }

        if ("dashboard".equalsIgnoreCase(action)) {
            List<ParkingLot> report = assignmentDAO.getOccupancyReport();
            request.setAttribute("report", report);

            request.getRequestDispatcher("parking_dashboard.jsp").forward(request, response);
            return; // <--- CRUCIAL: Detiene la ejecución aquí
        }

        // Si no entró en nada de lo anterior, redirigir al menú por defecto
        response.sendRedirect("main_menu.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String plate = request.getParameter("plateVehicle");
        String lotIdStr = request.getParameter("idParkingLot");

        if (plate != null && lotIdStr != null) {
            int idLot = Integer.parseInt(lotIdStr);

            // 1. Validar si el parqueo tiene cupo
            if (assignmentDAO.hasCapacity(idLot)) {
                // 2. Intentar registrar el ingreso
                boolean success = assignmentDAO.insert(plate, idLot);
                if (success) {
                    // CORRECCIÓN AQUÍ: Cambia .jsp por .html
                    response.sendRedirect("main_menu.html?msg=success");
                } else {
                    response.sendRedirect("assignments?action=prepare&error=db");
                }
            } else {
                // Si no hay cupo, devolvemos al formulario con un error
                response.sendRedirect("assignments?action=prepare&error=full");
            }
        }
    }
}