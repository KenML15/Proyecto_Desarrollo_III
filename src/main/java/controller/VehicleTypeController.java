package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.RateDAO;
import model.dao.VehicleTypeDAO;
import model.entity.Rate;
import model.entity.VehicleType;

@WebServlet("/vehicleTypes")
public class VehicleTypeController extends HttpServlet {

    private final VehicleTypeDAO typeDAO = new VehicleTypeDAO();
    private final RateDAO rateDAO = new RateDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"admin".equals(request.getSession().getAttribute("role"))) {
            response.sendRedirect("menu_clerk.jsp");
            return;
        }

        String action = request.getParameter("action");

        // editar tarifa
        if ("editRate".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            VehicleType vt = typeDAO.findById(id);
            Rate rate = rateDAO.getFullRateByVehicleType(id);

            request.setAttribute("vehicleType", vt);
            request.setAttribute("rate", rate != null ? rate : new Rate());
            request.getRequestDispatcher("edit_rate.jsp").forward(request, response);

        //nombre
        } else if ("editType".equals(action)) {

            int id = Integer.parseInt(request.getParameter("id"));
            VehicleType vt = typeDAO.findById(id);

            request.setAttribute("vehicleType", vt);
            request.getRequestDispatcher("edit_vehicle_type.jsp").forward(request, response);

        // lista
        } else {

            List<VehicleType> types = typeDAO.readAll();
            List<Rate> rates = rateDAO.findAll();

            request.setAttribute("types", types);
            request.setAttribute("rates", rates);
            request.getRequestDispatcher("manage_vehicle_types.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!"admin".equals(request.getSession().getAttribute("role"))) {
            response.sendRedirect("menu_clerk.jsp");
            return;
        }

        String action = request.getParameter("action");

        // agregar
        if ("addType".equals(action)) {

            String desc = request.getParameter("description").trim();
            typeDAO.insert(desc);
            response.sendRedirect("vehicleTypes?success=added");

        
        } else if ("updateType".equals(action)) {

            int id = Integer.parseInt(request.getParameter("idVehicleType"));
            String desc = request.getParameter("description").trim();
            typeDAO.update(new VehicleType(id, desc));
            response.sendRedirect("vehicleTypes?success=updated");

        } else if ("deleteType".equals(action)) {

            int id = Integer.parseInt(request.getParameter("idVehicleType"));
            boolean ok = typeDAO.delete(id);
            response.sendRedirect("vehicleTypes?" + (ok ? "success=deleted" : "error=cannot_delete"));

        } else if ("saveRate".equals(action)) {

            Rate r = new Rate();
            r.setIdVehicleType(Integer.parseInt(request.getParameter("idVehicleType")));
            r.setHalfHour(Float.parseFloat(request.getParameter("halfHour")));
            r.setHour(Float.parseFloat(request.getParameter("hour")));
            r.setDay(Float.parseFloat(request.getParameter("day")));
            r.setWeek(Float.parseFloat(request.getParameter("week")));
            r.setMonth(Float.parseFloat(request.getParameter("month")));
            r.setYear(Float.parseFloat(request.getParameter("year")));
            rateDAO.saveOrUpdate(r);
            response.sendRedirect("vehicleTypes?success=rate_saved");

        } else {
            response.sendRedirect("vehicleTypes");
        }
    }
}
