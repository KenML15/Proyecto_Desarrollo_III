package controller;

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

    private final ParkingLotDAO parkingDAO = new ParkingLotDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equalsIgnoreCase(action)) {
            parkingDAO.delete(Integer.parseInt(request.getParameter("id")));
            response.sendRedirect("parkingLot");

        } else if ("edit".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("parqueo", parkingDAO.findById(id));
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
        String name = request.getParameter("name");
        int numSpaces = Integer.parseInt(request.getParameter("num_spaces"));

        if ("update".equalsIgnoreCase(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            parkingDAO.update(new ParkingLot(id, name, numSpaces));
        } else {
            parkingDAO.insert(new ParkingLot(0, name, numSpaces));
        }

        response.sendRedirect("parkingLot");
    }
}