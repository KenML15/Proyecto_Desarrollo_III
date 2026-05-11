package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.VehicleDAO;
import model.dao.VehicleTypeDAO;
import model.dao.CustomerDAO;
import model.entity.Vehicle;
import model.entity.VehicleType;
import model.entity.Customer;

/**
 * @author Jefferson
 * @author Kenneth
 */
@WebServlet("/vehicles")
public class VehicleController extends HttpServlet {

    private VehicleDAO vehicleDAO;
    private VehicleTypeDAO typeDAO;
    private CustomerDAO customerDAO;

    public VehicleController() {
        vehicleDAO = new VehicleDAO();
        typeDAO = new VehicleTypeDAO();
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // --- 1. PREPARAR INSERCIÓN ---
        if ("add".equalsIgnoreCase(action)) {
            request.setAttribute("customers", customerDAO.findAll());
            request.setAttribute("vehicleTypes", typeDAO.readAll());
            request.getRequestDispatcher("insert_vehicle.jsp").forward(request, response);

        // --- 2. PREPARAR ASIGNACIÓN DE DUEÑO (REGLA #3) ---
        } else if ("prepareAssign".equalsIgnoreCase(action)) {
            request.setAttribute("vehicles", vehicleDAO.findAll());
            request.setAttribute("customers", customerDAO.findAll());
            request.getRequestDispatcher("assign_owner.jsp").forward(request, response);

        // --- 3. ELIMINAR VEHÍCULO ---
        } else if ("delete".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            vehicleDAO.delete(plate);
            response.sendRedirect("vehicles?msg=deleted");

        // --- 4. EDITAR VEHÍCULO (Usando el JSP que fusionamos antes) ---
        } else if ("edit".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            Vehicle vehicle = vehicleDAO.findByPlate(plate);
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("customers", customerDAO.findAll());
            request.setAttribute("vehicleTypes", typeDAO.readAll());
            request.getRequestDispatcher("edit_vehicle.jsp").forward(request, response);

        // --- POR DEFECTO: LISTAR TODO ---
        } else {
            List<Vehicle> list = vehicleDAO.findAll();
            request.setAttribute("vehicles", list);
            request.getRequestDispatcher("show_all_vehicles.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // --- ACCIÓN: ASIGNAR DUEÑO A VEHÍCULO EXISTENTE ---
        if ("assignOwner".equalsIgnoreCase(action)) {
            try {
                String plate = request.getParameter("plate");
                int idCustomer = Integer.parseInt(request.getParameter("idCustomer"));

                // Validación de Regla de Negocio: Máximo 2 dueños
                if (vehicleDAO.countOwners(plate) >= 2) {
                    response.sendRedirect("vehicles?action=prepareAssign&error=limit_reached");
                    return;
                }

                if (vehicleDAO.assignCustomer(plate, idCustomer)) {
                    response.sendRedirect("vehicles?msg=assigned");
                } else {
                    response.sendRedirect("vehicles?action=prepareAssign&error=db");
                }
            } catch (Exception e) {
                response.sendRedirect("vehicles?action=prepareAssign&error=data");
            }
            return;
        }

        // --- ACCIÓN: INSERTAR O ACTUALIZAR VEHÍCULO ---
        try {
            String plate = request.getParameter("plate");
            String color = request.getParameter("color");
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            int typeId = Integer.parseInt(request.getParameter("typeId"));
            int idCustomer = Integer.parseInt(request.getParameter("idCustomer"));

            Vehicle vehicle = new Vehicle(plate, color, brand, model, typeId);
            boolean success = false;

            if ("update".equalsIgnoreCase(action)) {
                // Actualizar datos básicos
                success = vehicleDAO.update(vehicle, typeId);
                // Solo refrescar el dueño si se seleccionó uno válido
                if (success && idCustomer > 0) {
                    vehicleDAO.assignCustomer(plate, idCustomer);
                }
            } else {
                // Inserción nueva: insert(vehicle, typeId) y luego asignar dueño si se indicó
                success = vehicleDAO.insert(vehicle, typeId);
                if (success && idCustomer > 0) {
                    vehicleDAO.assignCustomer(plate, idCustomer);
                }
            }

            if (success) {
                response.sendRedirect("vehicles?msg=success");
            } else {
                response.sendRedirect("vehicles?action=add&error=db");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("vehicles?action=add&error=data");
        }
    }
}