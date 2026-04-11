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
import model.entity.Space;

public class SpaceDAO {

    // 1. Buscar todos los espacios disponibles
    public List<Space> findAvailable() {
        List<Space> list = new ArrayList<>();
        String sql = "SELECT * FROM space WHERE space_taken = FALSE";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Space(
                    rs.getInt("id"),
                    rs.getBoolean("disability_adaptation"),
                    rs.getBoolean("space_taken")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar espacios: " + e.getMessage());
        }
        return list;
    }

    // 2. Cambiar el estado del espacio (Ocupar o Liberar)
    public boolean updateStatus(int id, boolean isTaken) {
        String sql = "UPDATE space SET space_taken = ? WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBoolean(1, isTaken);
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al actualizar espacio: " + e.getMessage());
            return false;
        }
    }
}