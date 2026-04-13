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

        if ("delete".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            vehicleDAO.delete(plate);
            response.sendRedirect("vehicles");

        } else if ("edit".equalsIgnoreCase(action)) {
            String plate = request.getParameter("plate");
            Vehicle vehicle = vehicleDAO.findByPlate(plate);
            request.setAttribute("vehicle", vehicle);

            request.getRequestDispatcher("edit_vehicle.jsp").forward(request, response);

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

    try {
        String plate = request.getParameter("plate");
        String color = request.getParameter("color");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        
        // Buscamos "typeId" que es como lo nombramos en el JSP
        String typeIdStr = request.getParameter("typeId");
        int typeId = (typeIdStr != null && !typeIdStr.isEmpty()) ? Integer.parseInt(typeIdStr) : 1;

        Vehicle vehicle = new Vehicle(plate, color, brand, model);

        boolean success = false;
        if ("update".equalsIgnoreCase(action)) {
            success = vehicleDAO.update(vehicle, typeId);
        } else {
            success = vehicleDAO.insert(vehicle, typeId);
        }

        if (success) {
            response.sendRedirect("vehicles");
        } else {
            // Si el DAO devuelve false, algo falló en la BD (llave foránea, placa duplicada, etc)
            System.out.println("Error: El DAO no pudo insertar en la BD.");
            response.sendRedirect("insert_vehicle.jsp?error=db");
        }

    } catch (NumberFormatException e) {
        System.out.println("Error en datos numéricos: " + e.getMessage());
        response.sendRedirect("insert_vehicle.jsp?error=format");
    }
}
}