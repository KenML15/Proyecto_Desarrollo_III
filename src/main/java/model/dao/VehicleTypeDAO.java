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

    public List<VehicleType> findAll() {
        List<VehicleType> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle_type";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                VehicleType vType = new VehicleType(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getInt("number_of_tires"),
                    rs.getFloat("fee")
                );
                list.add(vType);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al listar tipos: " + e.getMessage());
        }
        return list;
    }

    public boolean insert(VehicleType type) {
        String sql = "INSERT INTO vehicle_type (description, number_of_tires, fee) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, type.getDescription());
            pstmt.setInt(2, type.getNumberOfTires());
            pstmt.setFloat(3, type.getFee());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
