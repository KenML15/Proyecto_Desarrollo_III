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
import model.entity.Ticket;

public class TicketDAO {

    // 1. Registrar entrada (Crear Ticket)
    public boolean openTicket(int customerId, String plate, int spaceId) {
        String sql = "INSERT INTO ticket (entry_date, customer_id, vehicle_plate, space_id) VALUES (NOW(), ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            pstmt.setString(2, plate);
            pstmt.setInt(3, spaceId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al abrir ticket: " + e.getMessage());
            return false;
        }
    }

    // 2. Registrar salida y calcular monto (Cerrar Ticket)
    public boolean closeTicket(int ticketId, float finalAmount) {
        String sql = "UPDATE ticket SET exit_date = NOW(), total_amount = ? WHERE id = ?";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setFloat(1, finalAmount);
            pstmt.setInt(2, ticketId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al cerrar ticket: " + e.getMessage());
            return false;
        }
    }

    // 3. Buscar ticket activo por placa (Para el proceso de salida)
    public int findActiveTicketByPlate(String plate) {
        String sql = "SELECT id FROM ticket WHERE vehicle_plate = ? AND exit_date IS NULL";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // No se encontró ticket activo
    }
}