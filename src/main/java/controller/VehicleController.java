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
import model.entity.Vehicle;
import model.entity.VehicleType;

/**
 *
 * @author Jefferson
 */
@WebServlet("/vehicles")
public class VehicleController extends HttpServlet {

    private VehicleDAO vehicleDAO;

    public VehicleController() {
        vehicleDAO = new VehicleDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            //capturar
            String plate = request.getParameter("plate");
            String color = request.getParameter("color");
            String brand = request.getParameter("brand");
            String model = request.getParameter("model");
            
            int idVehicleType = Integer.parseInt(request.getParameter("id")); 

            //crear
            //uso de constructor temporal 14 abril
            Vehicle vehicle = new Vehicle(plate, color, brand, model);

            if ("update".equalsIgnoreCase(action)) {
                vehicleDAO.update(vehicle); 
            } else {
                vehicleDAO.insert(vehicle, idVehicleType);
            }

            response.sendRedirect("vehicles");

        } catch (NumberFormatException e) {
            System.out.println("Error: El ID del tipo de vehículo debe ser numérico.");
            response.sendRedirect("vehicles");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            List<Vehicle> list = vehicleDAO.findAll();
            request.setAttribute("vehicles", list); 
            RequestDispatcher dispatcher = request.getRequestDispatcher("show_all_vehicles.jsp");
            dispatcher.forward(request, response);
            
        } else if ("edit".equalsIgnoreCase(action)) {
            //faltaria buscaar, editar, etc
        }
    }
}