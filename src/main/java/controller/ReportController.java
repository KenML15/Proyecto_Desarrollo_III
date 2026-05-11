package controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.dao.TicketDAO;
import model.dao.ParkingLotDAO;
import model.dao.RateDAO;
import model.entity.ParkingLot;
import model.entity.Rate;
import model.entity.Ticket;

/**
 * Generates 3 business PDF reports:
 *  1. Daily income report  (report=income)
 *  2. Occupancy summary    (report=occupancy)
 *  3. Closed tickets list  (report=tickets)
 */
@WebServlet("/reports")
public class ReportController extends HttpServlet {

    // ── Shared fonts & colors ──────────────────────────────────────────────
    private static final BaseColor COLOR_HEADER  = new BaseColor(15,  23,  42);   // dark navy
    private static final BaseColor COLOR_ACCENT  = new BaseColor(78, 204, 163);   // teal
    private static final BaseColor COLOR_ROW_ALT = new BaseColor(240, 248, 255);  // light blue-white
    private static final Font FONT_TITLE   = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD,   BaseColor.WHITE);
    private static final Font FONT_SECTION = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   new BaseColor(15, 23, 42));
    private static final Font FONT_BODY    = new Font(Font.FontFamily.HELVETICA, 9,  Font.NORMAL, BaseColor.DARK_GRAY);
    private static final Font FONT_BODY_BOLD = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD,  BaseColor.DARK_GRAY);
    private static final Font FONT_COL_HDR = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   BaseColor.WHITE);

    private final TicketDAO ticketDAO       = new TicketDAO();
    private final ParkingLotDAO lotDAO      = new ParkingLotDAO();
    private final RateDAO rateDAO           = new RateDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        String role = (String) request.getSession().getAttribute("role");
        if (!"admin".equals(role)) {
            response.sendRedirect("menu_clerk.jsp");
            return;
        }

        String report = request.getParameter("report");
        if (report == null) report = "income";

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"reporte_" + report + ".pdf\"");

        try {
            switch (report) {
                case "occupancy": generateOccupancyReport(response); break;
                case "tickets":   generateClosedTicketsReport(response); break;
                default:          generateIncomeReport(response); break;
            }
        } catch (DocumentException e) {
            throw new ServletException("Error generating PDF report", e);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // REPORT 1 – Daily Income Summary
    // ═══════════════════════════════════════════════════════════════════════
    private void generateIncomeReport(HttpServletResponse response)
            throws DocumentException, IOException {

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        addReportHeader(doc, "Reporte de Ingresos", "Resumen de cobros registrados en el sistema");

        List<Ticket> tickets = ticketDAO.findAll();

        // ── Totals ──────────────────────────────────────────────────────────
        double totalIncome = 0;
        int closedCount = 0;
        int activeCount = 0;
        double disabilityDiscount = 0;

        for (Ticket t : tickets) {
            if (t.getExitDate() != null) {
                closedCount++;
                totalIncome += t.getTotalAmount();
                if (t.getCustomer() != null && t.getCustomer().isDisabilityPresented()) {
                    // estimate what was discounted: cobrado = 50% → descuento = cobrado
                    disabilityDiscount += t.getTotalAmount();
                }
            } else {
                activeCount++;
            }
        }

        // Summary box
        addSectionTitle(doc, "Resumen General");
        PdfPTable summary = new PdfPTable(2);
        summary.setWidthPercentage(60);
        summary.setHorizontalAlignment(Element.ALIGN_LEFT);
        summary.setSpacingBefore(6);
        summary.setSpacingAfter(14);
        addSummaryRow(summary, "Total Ingresos Cobrados", String.format("₡ %,.2f", totalIncome));
        addSummaryRow(summary, "Tiquetes Cerrados", String.valueOf(closedCount));
        addSummaryRow(summary, "Vehículos Aún en Parqueo", String.valueOf(activeCount));
        addSummaryRow(summary, "Descuentos Ley 7600 (50%)", String.format("₡ %,.2f", disabilityDiscount));
        doc.add(summary);

        // Detailed table
        addSectionTitle(doc, "Detalle de Tiquetes Cerrados");
        PdfPTable table = new PdfPTable(new float[]{1f, 2f, 2.5f, 2.5f, 2.5f, 2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        addTableHeader(table, "#", "Placa", "Cliente", "Entrada", "Salida", "Monto");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        boolean alt = false;
        for (Ticket t : tickets) {
            if (t.getExitDate() == null) continue;
            BaseColor bg = alt ? COLOR_ROW_ALT : BaseColor.WHITE;
            addBodyCell(table, String.valueOf(t.getId()), bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getVehicle() != null ? t.getVehicle().getPlate() : "—", bg, Element.ALIGN_CENTER);
            String clientLabel = t.getCustomer() != null ? t.getCustomer().getName() : "—";
            if (t.getCustomer() != null && t.getCustomer().isDisabilityPresented())
                clientLabel += " ★";
            addBodyCell(table, clientLabel, bg, Element.ALIGN_LEFT);
            addBodyCell(table, t.getEntryDate() != null ? t.getEntryDate().format(fmt) : "—", bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getExitDate().format(fmt), bg, Element.ALIGN_CENTER);
            addBodyCell(table, String.format("₡%,.2f", t.getTotalAmount()), bg, Element.ALIGN_RIGHT);
            alt = !alt;
        }
        doc.add(table);

        Paragraph note = new Paragraph("★ = Cliente con descuento Ley 7600 (50% aplicado)", FONT_BODY);
        note.setSpacingBefore(6);
        doc.add(note);

        addFooter(doc);
        doc.close();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // REPORT 2 – Parking Lot Occupancy
    // ═══════════════════════════════════════════════════════════════════════
    private void generateOccupancyReport(HttpServletResponse response)
            throws DocumentException, IOException {

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        addReportHeader(doc, "Reporte de Ocupación", "Estado actual de espacios por parqueo");

        List<ParkingLot> lots = lotDAO.findAll();

        addSectionTitle(doc, "Ocupación por Parqueo");
        PdfPTable table = new PdfPTable(new float[]{3f, 2f, 2f, 2f, 2f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        addTableHeader(table, "Parqueo", "Espacios Totales", "Ocupados", "Disponibles", "% Ocupación");

        boolean alt = false;
        int grandTotal = 0, grandOccupied = 0;

        for (ParkingLot lot : lots) {
            int total = lot.getNumberOfSpaces();
            // Count active tickets for this lot using a simple approach
            int occupied = ticketDAO.countActiveTicketsByLotId(lot.getId());
            int available = Math.max(0, total - occupied);
            double pct = total > 0 ? (occupied * 100.0 / total) : 0;

            grandTotal += total;
            grandOccupied += occupied;

            BaseColor bg = alt ? COLOR_ROW_ALT : BaseColor.WHITE;
            addBodyCell(table, lot.getName(), bg, Element.ALIGN_LEFT);
            addBodyCell(table, String.valueOf(total), bg, Element.ALIGN_CENTER);
            addBodyCell(table, String.valueOf(occupied), bg, Element.ALIGN_CENTER);
            addBodyCell(table, String.valueOf(available), bg, Element.ALIGN_CENTER);
            addBodyCell(table, String.format("%.1f%%", pct), bg, Element.ALIGN_CENTER);
            alt = !alt;
        }

        // Totals row
        double grandPct = grandTotal > 0 ? (grandOccupied * 100.0 / grandTotal) : 0;
        PdfPCell totalLabelCell = styledCell("TOTAL GENERAL", FONT_COL_HDR, COLOR_HEADER, Element.ALIGN_LEFT);
        table.addCell(totalLabelCell);
        table.addCell(styledCell(String.valueOf(grandTotal), FONT_COL_HDR, COLOR_HEADER, Element.ALIGN_CENTER));
        table.addCell(styledCell(String.valueOf(grandOccupied), FONT_COL_HDR, COLOR_HEADER, Element.ALIGN_CENTER));
        table.addCell(styledCell(String.valueOf(grandTotal - grandOccupied), FONT_COL_HDR, COLOR_HEADER, Element.ALIGN_CENTER));
        table.addCell(styledCell(String.format("%.1f%%", grandPct), FONT_COL_HDR, COLOR_HEADER, Element.ALIGN_CENTER));

        doc.add(table);
        addFooter(doc);
        doc.close();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // REPORT 3 – Closed Tickets with Rate Details
    // ═══════════════════════════════════════════════════════════════════════
    private void generateClosedTicketsReport(HttpServletResponse response)
            throws DocumentException, IOException {

        Document doc = new Document(PageSize.A4.rotate()); // Landscape
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        addReportHeader(doc, "Historial Completo de Tiquetes", "Todos los tiquetes cerrados con detalle de tarifa");

        List<Ticket> tickets = ticketDAO.findAll();
        List<Rate> rates = rateDAO.findAll();

        addSectionTitle(doc, "Tiquetes Cerrados – Detalle Completo");
        PdfPTable table = new PdfPTable(new float[]{0.6f, 1.8f, 2.2f, 1.8f, 2f, 2f, 1.5f, 1.8f, 1.4f});
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);
        addTableHeader(table, "#", "Placa", "Cliente", "Tipo Veh.", "Entrada", "Salida", "Duración", "Monto", "Desc.");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
        boolean alt = false;

        for (Ticket t : tickets) {
            if (t.getExitDate() == null) continue;
            BaseColor bg = alt ? COLOR_ROW_ALT : BaseColor.WHITE;

            String duration = computeDuration(t);
            boolean hasDisc = t.getCustomer() != null && t.getCustomer().isDisabilityPresented();
            String typeDesc = t.getVehicle() != null ? t.getVehicle().getVehicleTypeDesc() : "—";

            addBodyCell(table, String.valueOf(t.getId()), bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getVehicle() != null ? t.getVehicle().getPlate() : "—", bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getCustomer() != null ? t.getCustomer().getName() : "—", bg, Element.ALIGN_LEFT);
            addBodyCell(table, typeDesc, bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getEntryDate() != null ? t.getEntryDate().format(fmt) : "—", bg, Element.ALIGN_CENTER);
            addBodyCell(table, t.getExitDate().format(fmt), bg, Element.ALIGN_CENTER);
            addBodyCell(table, duration, bg, Element.ALIGN_CENTER);
            addBodyCell(table, String.format("₡%,.2f", t.getTotalAmount()), bg, Element.ALIGN_RIGHT);
            addBodyCell(table, hasDisc ? "50% ★" : "—", bg, Element.ALIGN_CENTER);
            alt = !alt;
        }
        doc.add(table);

        Paragraph note = new Paragraph("★ = Descuento Ley 7600 aplicado (50%). Monto mostrado ya incluye el descuento.", FONT_BODY);
        note.setSpacingBefore(8);
        doc.add(note);

        addFooter(doc);
        doc.close();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SHARED HELPERS
    // ═══════════════════════════════════════════════════════════════════════

    private void addReportHeader(Document doc, String title, String subtitle)
            throws DocumentException {
        PdfPTable header = new PdfPTable(1);
        header.setWidthPercentage(100);
        header.setSpacingAfter(16);

        PdfPCell titleCell = new PdfPCell(new Phrase(title, FONT_TITLE));
        titleCell.setBackgroundColor(COLOR_HEADER);
        titleCell.setPadding(14);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        header.addCell(titleCell);

        doc.add(header);

        Paragraph sub = new Paragraph(subtitle, new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
        sub.setSpacingAfter(4);
        doc.add(sub);

        String ts = "Generado el " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Paragraph date = new Paragraph(ts, FONT_BODY);
        date.setSpacingAfter(14);
        doc.add(date);

        doc.add(new LineSeparator(0.5f, 100, COLOR_ACCENT, Element.ALIGN_CENTER, -2));
        doc.add(Chunk.NEWLINE);
    }

    private void addSectionTitle(Document doc, String title) throws DocumentException {
        Paragraph p = new Paragraph(title, FONT_SECTION);
        p.setSpacingBefore(12);
        p.setSpacingAfter(4);
        doc.add(p);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, FONT_COL_HDR));
            cell.setBackgroundColor(COLOR_HEADER);
            cell.setPadding(6);
            cell.setBorderColor(COLOR_ACCENT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addBodyCell(PdfPTable table, String text, BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "—", FONT_BODY));
        cell.setBackgroundColor(bg);
        cell.setPadding(5);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        cell.setHorizontalAlignment(align);
        table.addCell(cell);
    }

    private PdfPCell styledCell(String text, Font font, BaseColor bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setPadding(6);
        cell.setBorderColor(COLOR_ACCENT);
        cell.setHorizontalAlignment(align);
        return cell;
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell lbl = new PdfPCell(new Phrase(label, FONT_BODY_BOLD));
        lbl.setPadding(5);
        lbl.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value, FONT_BODY));
        val.setPadding(5);
        val.setBorderColor(new BaseColor(220, 220, 220));
        val.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(val);
    }

    private void addFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        doc.add(new LineSeparator(0.5f, 100, COLOR_ACCENT, Element.ALIGN_CENTER, -2));
        Paragraph footer = new Paragraph("Sistema de Gestión de Parqueos – Reporte generado automáticamente", FONT_BODY);
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(6);
        doc.add(footer);
    }

    /**
     * Computes a human-readable duration string directly from a Ticket,
     * used in the PDF report (Ticket.getStayDuration() exists in the entity
     * but we replicate logic here to avoid JSP coupling).
     */
    private String computeDuration(Ticket t) {
        if (t.getEntryDate() == null || t.getExitDate() == null) return "—";
        long totalMinutes = java.time.Duration.between(t.getEntryDate(), t.getExitDate()).toMinutes();
        if (totalMinutes < 1) return "<1 min";
        long h = totalMinutes / 60;
        long m = totalMinutes % 60;
        if (h == 0) return m + " min";
        if (m == 0) return h + " h";
        return h + " h " + m + " min";
    }
}