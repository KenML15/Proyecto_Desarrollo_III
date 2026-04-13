/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import model.entity.Vehicle;
import model.entity.VehicleType;

/**
 *
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
            request.setAttribute("listaVehiculos", list);
            request.getRequestDispatcher("show_all_vehicles.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        String plate = request.getParameter("plate");
        String color = request.getParameter("color");
        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        int typeId = Integer.parseInt(request.getParameter("id"));

        Vehicle vehicle = new Vehicle(plate, color, brand, model);

        if ("update".equalsIgnoreCase(action)) {
            vehicleDAO.update(vehicle, typeId);
        } else {
            vehicleDAO.insert(vehicle, typeId);
        }

        response.sendRedirect("vehicles");
    }
}
