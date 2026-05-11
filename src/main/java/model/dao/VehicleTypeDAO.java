package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.VehicleType;

public class VehicleTypeDAO {

    public boolean insert(String description) {
        String sql = "INSERT INTO vehicle_type (description) VALUES (?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, description);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<VehicleType> readAll() {
        List<VehicleType> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle_type ORDER BY id_vehicle_type";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new VehicleType(rs.getInt("id_vehicle_type"), rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public VehicleType findById(int id) {
        String sql = "SELECT * FROM vehicle_type WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new VehicleType(rs.getInt("id_vehicle_type"), rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean update(VehicleType vt) {
        String sql = "UPDATE vehicle_type SET description = ? WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, vt.getDescription());
            ps.setInt(2, vt.getIdVehicleType());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM vehicle_type WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Cannot delete vehicle type: vehicles of this type may exist.");
            return false;
        }
    }
}