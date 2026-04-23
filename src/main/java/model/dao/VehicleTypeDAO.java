/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

/**
 *
 * @author Kenneth
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.VehicleType;

public class VehicleTypeDAO {

    //create
    public boolean create(String description) {
        String sql = "INSERT INTO vehicle_type (description) VALUES (?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //read
    public List<VehicleType> readAll() {
        List<VehicleType> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle_type";
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

    //upd
    public boolean update(VehicleType vt) {
        String sql = "UPDATE vehicle_type SET description = ? WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vt.getDescription());
            pstmt.setInt(2, vt.getIdVehicleType());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete
    public boolean delete(int id) {
        String sql = "DELETE FROM vehicle_type WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error: No puedes borrar un tipo si tiene vehículos o tarifas asociados.");
            return false;
        }
    }
}
