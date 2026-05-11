package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.RateDAO;
import model.entity.Rate;

@WebServlet("/rates")
public class RateController extends HttpServlet {

    private final RateDAO rateDAO = new RateDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("idVehicleType");
        if (idParam != null) {
            int idType = Integer.parseInt(idParam);
            request.setAttribute("rate", rateDAO.getFullRateByVehicleType(idType));
            request.setAttribute("idVehicleType", idType);
            request.getRequestDispatcher("manage_rates.jsp").forward(request, response);
        } else {
            response.sendRedirect("vehicleTypes");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Rate r = new Rate();
        r.setIdVehicleType(Integer.parseInt(request.getParameter("idVehicleType")));
        r.setHalfHour(Float.parseFloat(request.getParameter("halfHour")));
        r.setHour(Float.parseFloat(request.getParameter("hour")));
        r.setDay(Float.parseFloat(request.getParameter("day")));
        r.setWeek(Float.parseFloat(request.getParameter("week")));
        r.setMonth(Float.parseFloat(request.getParameter("month")));
        r.setYear(Float.parseFloat(request.getParameter("year")));

        if (rateDAO.saveOrUpdate(r)) {
            response.sendRedirect("vehicleTypes?msg=success");
        } else {
            response.sendRedirect("rates?idVehicleType=" + r.getIdVehicleType() + "&error=1");
        }
    }
}