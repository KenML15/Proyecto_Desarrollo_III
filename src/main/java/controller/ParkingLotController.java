/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Kenneth
 */
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.ParkingLotDAO;
import model.entity.ParkingLot;

@WebServlet("/parkingLot")
public class ParkingLotController extends HttpServlet {

    private ParkingLotDAO parkingDAO = new ParkingLotDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            parkingDAO.delete(id);
            response.sendRedirect("parkingLot");

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            ParkingLot p = parkingDAO.findById(id);
            request.setAttribute("parqueo", p);
            request.getRequestDispatcher("edit_parkinglot.jsp").forward(request, response);

        } else {
            List<ParkingLot> list = parkingDAO.findAll();
            request.setAttribute("listaParqueos", list);
            request.getRequestDispatcher("show_all_parkingslots.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        int numSpaces = Integer.parseInt(request.getParameter("num_spaces"));

        ParkingLot p = new ParkingLot(id, name, numSpaces);

        if ("update".equalsIgnoreCase(action)) {
            parkingDAO.update(p);
        } else {
            parkingDAO.insert(p);
        }

        response.sendRedirect("parkingLot");
    }
}
