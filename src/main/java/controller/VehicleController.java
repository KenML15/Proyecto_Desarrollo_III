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
import model.dao.VehicleDAO;
import model.dao.VehicleTypeDAO;
import model.entity.Vehicle;
import model.entity.VehicleType;

/**
 *
 * @author Jefferson
 * @author Kenneth
 */
@WebServlet("/vehicles")
public class VehicleController extends HttpServlet {

    private VehicleDAO vehicleDAO;
    private VehicleTypeDAO typeDAO;

    public VehicleController() {
        vehicleDAO = new VehicleDAO();
        typeDAO = new VehicleTypeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equalsIgnoreCase(action)) {
            // Ahora solo necesitamos los tipos de vehículo, no los clientes
            request.setAttribute("types", typeDAO.readAll());
            request.getRequestDispatcher("insert_vehicle.jsp").forward(request, response);

        } else if ("prepareAssign".equalsIgnoreCase(action)) {
            // NUEVO: Carga datos para la nueva pantalla de asignación
            model.dao.CustomerDAO customerDAO = new model.dao.CustomerDAO();
            request.setAttribute("vehicles", vehicleDAO.findAll());
            request.setAttribute("customers", customerDAO.findAll());
            request.getRequestDispatcher("assign_owner.jsp").forward(request, response);
            // Si la acción es eliminar
        } else if ("delete".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            vehicleDAO.delete(plate);
            response.sendRedirect("vehicles?action=list");

            // Si la acción es editar
        } else if ("edit".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            model.entity.Vehicle vehicle = vehicleDAO.findByPlate(plate);
            model.dao.CustomerDAO customerDAO = new model.dao.CustomerDAO();
            request.setAttribute("vehicle", vehicle);
            request.setAttribute("customers", customerDAO.findAll());
            request.getRequestDispatcher("edit_vehicle.jsp").forward(request, response);

            // POR DEFECTO: Mostrar la lista de gestión
        } else {
            List<model.entity.Vehicle> list = vehicleDAO.findAll();
            request.setAttribute("vehicles", list);
            request.getRequestDispatcher("show_all_vehicles.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("assignOwner".equalsIgnoreCase(action)) {
            try {
                String plate = request.getParameter("plate");
                int idCustomer = Integer.parseInt(request.getParameter("idCustomer"));

                // Validar Regla de Negocio #3 (Máximo 2 dueños)
                int currentOwners = vehicleDAO.countOwners(plate);
                if (currentOwners >= 2) {
                    response.sendRedirect("vehicles?action=prepareAssign&error=limit_reached&plate=" + plate);
                    return;
                }

                // Intentar la asignación
                boolean success = vehicleDAO.assignCustomer(plate, idCustomer);
                if (success) {
                    response.sendRedirect("menu_admin.jsp?msg=assigned");
                } else {
                    // Si falla aquí, saldrá el error de la imagen
                    response.sendRedirect("vehicles?action=prepareAssign&error=db");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("vehicles?action=prepareAssign&error=db");
            }
            return; // Importante para que no intente ejecutar el código de abajo
        }

        // ACCIÓN ORIGINAL: INSERTAR VEHÍCULO (Ya sin idCustomer)
        try {
            String plate = request.getParameter("plate");
            String color = request.getParameter("color");
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            int typeId = Integer.parseInt(request.getParameter("typeId"));

            Vehicle v = new Vehicle(plate, color, brand, model);
            boolean success = vehicleDAO.insert(v, typeId); // Tu insert ya no necesita idCustomer aquí

            if (success) {
                response.sendRedirect("vehicles?msg=created");
            } else {
                response.sendRedirect("insert_vehicle.jsp?error=db");
            }
        } catch (Exception e) {
            response.sendRedirect("insert_vehicle.jsp?error=data");
        }
    }
}
