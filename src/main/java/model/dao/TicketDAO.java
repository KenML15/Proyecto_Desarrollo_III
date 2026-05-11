package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.Customer;
import model.entity.Ticket;
import model.entity.Vehicle;

public class TicketDAO {

    public int openTicket(int idCustomer, String plate) {
        String sql = "INSERT INTO ticket (entry_date, id_customer, vehicle_plate) VALUES (NOW(), ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idCustomer);
            ps.setString(2, plate);

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error opening ticket: " + e.getMessage());
        }
        return -1;
    }

    public boolean closeTicket(int ticketId, float finalAmount) {
        String sql = "UPDATE ticket SET exit_date = NOW(), total_amount = ? WHERE id = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setFloat(1, finalAmount);
            ps.setInt(2, ticketId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error closing ticket: " + e.getMessage());
            return false;
        }
    }

    public int findActiveTicketByPlate(String plate) {
        String sql = "SELECT id FROM ticket WHERE vehicle_plate = ? AND exit_date IS NULL";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Ticket getActiveTicketDetails(String plate) {
        String sql = "SELECT t.id, t.entry_date, t.id_customer, " +
                     "       c.name AS customer_name, c.disability, " +
                     "       v.plate, v.brand, v.model, v.color, v.id_vehicle_type, " +
                     "       vt.description AS type_desc " +
                     "FROM ticket t " +
                     "JOIN customer c ON t.id_customer = c.id " +
                     "JOIN vehicle  v ON t.vehicle_plate = v.plate " +
                     "JOIN vehicle_type vt ON v.id_vehicle_type = vt.id_vehicle_type " +
                     "WHERE t.vehicle_plate = ? AND t.exit_date IS NULL LIMIT 1";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapFullTicket(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Ticket findById(int id) {
        String sql = "SELECT t.id, t.entry_date, t.exit_date, t.total_amount, t.id_customer, " +
                     "       c.name AS customer_name, c.disability, " +
                     "       v.plate, v.brand, v.model, v.color, v.id_vehicle_type, " +
                     "       vt.description AS type_desc " +
                     "FROM ticket t " +
                     "JOIN customer c ON t.id_customer = c.id " +
                     "JOIN vehicle  v ON t.vehicle_plate = v.plate " +
                     "JOIN vehicle_type vt ON v.id_vehicle_type = vt.id_vehicle_type " +
                     "WHERE t.id = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ticket t = mapFullTicket(rs);
                Timestamp exitTs = rs.getTimestamp("exit_date");
                if (exitTs != null) t.setExitDate(exitTs.toLocalDateTime());
                t.setTotalAmount(rs.getFloat("total_amount"));
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ticket> findAll() {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT t.id, t.entry_date, t.exit_date, t.total_amount, " +
                     "       c.id AS cid, c.name AS cname, c.disability, " +
                     "       v.plate, v.brand, v.model, v.color, v.id_vehicle_type, " +
                     "       vt.description AS type_desc " +
                     "FROM ticket t " +
                     "JOIN customer c ON t.id_customer = c.id " +
                     "JOIN vehicle  v ON t.vehicle_plate = v.plate " +
                     "JOIN vehicle_type vt ON v.id_vehicle_type = vt.id_vehicle_type " +
                     "ORDER BY t.entry_date DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket();
                t.setId(rs.getInt("id"));
                t.setEntryDate(rs.getTimestamp("entry_date").toLocalDateTime());

                Timestamp exitTs = rs.getTimestamp("exit_date");
                if (exitTs != null) t.setExitDate(exitTs.toLocalDateTime());
                t.setTotalAmount(rs.getFloat("total_amount"));

                Customer c = new Customer();
                c.setId(rs.getInt("cid"));
                c.setName(rs.getString("cname"));
                c.setDisabilityPresented(rs.getBoolean("disability"));
                t.setCustomer(c);

                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                v.setColor(rs.getString("color"));
                v.setIdVehicleType(rs.getInt("id_vehicle_type"));
                v.setVehicleTypeDesc(rs.getString("type_desc"));
                t.setVehicle(v);

                list.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error listing tickets: " + e.getMessage());
        }
        return list;
    }

    public int countActiveTicketsByLotId(int lotId) {
        String sql = "SELECT COUNT(*) FROM ticket t " +
                     "JOIN vehicle_assignment va ON t.vehicle_plate = va.plate_vehicle " +
                     "WHERE t.exit_date IS NULL AND UPPER(TRIM(va.status)) = 'ACTIVE' AND va.id_parking_lot = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, lotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Ticket mapFullTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setId(rs.getInt("id"));
        t.setEntryDate(rs.getTimestamp("entry_date").toLocalDateTime());

        Customer c = new Customer();
        c.setName(rs.getString("customer_name"));
        c.setDisabilityPresented(rs.getBoolean("disability"));
        t.setCustomer(c);

        Vehicle v = new Vehicle();
        v.setPlate(rs.getString("plate"));
        v.setBrand(rs.getString("brand"));
        v.setModel(rs.getString("model"));
        v.setColor(rs.getString("color"));
        v.setIdVehicleType(rs.getInt("id_vehicle_type"));
        v.setVehicleTypeDesc(rs.getString("type_desc"));
        t.setVehicle(v);

        return t;
    }
}