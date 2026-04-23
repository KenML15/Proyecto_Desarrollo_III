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

public class TicketDAO {

    // 1. Registrar entrada (Crear Ticket)
    public int openTicket(int idCustomer, String plate) {
    String sql = "INSERT INTO ticket (entry_date, id_customer, vehicle_plate) VALUES (NOW(), ?, ?)";

    try (Connection conn = DbConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setInt(1, idCustomer);
        pstmt.setString(2, plate);

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows > 0) {
            // se recupera el id del autoincrement
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); //retorna
                }
            }
        }
        return -1; // Si no generó ID
    } catch (SQLException e) {
        System.out.println("Error al abrir ticket: " + e.getMessage());
        return -1;
    }
}

    // 2. Registrar salida y calcular monto (Cerrar Ticket)
    public boolean closeTicket(int ticketId, float finalAmount) {
        String sql = "UPDATE ticket SET exit_date = NOW(), total_amount = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // No se encontró ticket activo
    }
}
