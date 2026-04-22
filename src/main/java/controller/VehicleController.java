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

        // Si no hay acción o es "add", preparamos el formulario
        if ("add".equalsIgnoreCase(action)) {
            model.dao.CustomerDAO customerDAO = new model.dao.CustomerDAO();
            List<model.entity.Customer> customerList = customerDAO.findAll();

            request.setAttribute("customers", customerList);
            request.getRequestDispatcher("insert_vehicle.jsp").forward(request, response);

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

        try {
            String plate = request.getParameter("plate");
            String color = request.getParameter("color");
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");

            // Capturamos el tipo de vehículo
            String typeIdStr = request.getParameter("typeId");
            int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : 1;

            // NUEVO: Capturamos el ID del cliente desde el formulario
            String idCustStr = request.getParameter("idCustomer");
            int idCustomer = (idCustStr != null && !idCustStr.isEmpty()) ? Integer.parseInt(idCustStr) : 0;

            Vehicle vehicle = new Vehicle(plate, color, brand, model);
            boolean success = false;

            if ("update".equalsIgnoreCase(action)) {
                success = vehicleDAO.update(vehicle, typeId);
            } else {
                // AHORA USA LOS 3 PARÁMETROS:
                success = vehicleDAO.insert(vehicle, typeId, idCustomer);
            }

            // Si la inserción o actualización fue exitosa y se seleccionó un cliente
            if (success) {
                if (idCustomer > 0) {
                    vehicleDAO.assignCustomer(plate, idCustomer);
                }
                response.sendRedirect("vehicles");
            } else {
                System.out.println("Error: El DAO no pudo procesar en la BD.");
                response.sendRedirect("insert_vehicle.jsp?error=db");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error en datos numéricos: " + e.getMessage());
            response.sendRedirect("insert_vehicle.jsp?error=format");
        }
    }
}
