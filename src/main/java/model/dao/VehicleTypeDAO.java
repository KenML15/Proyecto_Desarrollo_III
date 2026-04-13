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
import model.entity.Vehicle;
import model.entity.VehicleType;

public class VehicleTypeDAO {

    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setColor(rs.getString("color"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                list.add(v);
            }
            System.out.println("LOG: Se cargaron " + list.size() + " vehículos del DAO.");
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(VehicleType type) {

        String sql = "INSERT IGNORE INTO vehicle_type (id, description, number_of_tires, fee) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, type.getId());
            pstmt.setString(2, type.getDescription());
            pstmt.setInt(3, type.getNumberOfTires());
            pstmt.setFloat(4, type.getFee());
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
