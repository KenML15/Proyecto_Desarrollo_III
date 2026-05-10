/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entity.ParkingLot;
import model.entity.Vehicle;

/**
 *
 * @author Kenneth
 */
public class ParkingLotDAO {

    public boolean insert(ParkingLot p) {
        // Quitamos 'id' de la consulta
        String sql = "INSERT INTO parking_lot (name, number_of_spaces) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getNumberOfSpaces());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error SQL en ParkingLotDAO: " + e.getMessage());
            return false;
        }
    }

    public List<ParkingLot> findAll() {
        List<ParkingLot> list = new ArrayList<>();

        String sql = "SELECT * FROM parking_lot";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ParkingLot p = new ParkingLot();

                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setNumberOfSpaces(rs.getInt("number_of_spaces"));
                list.add(p);
            }

            System.out.println(">>> DAO: Se recuperaron " + list.size() + " registros.");

        } catch (SQLException e) {
            System.out.println(">>> ERROR DAO: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(int id) {
    // 1. Borrar asignaciones (Historial)
    String sqlAsignaciones = "DELETE FROM vehicle_assignment WHERE id_parking_lot = ?";
    // 2. Borrar espacios (Slots configurados) - ESTA TE FALTA EN TU CÓDIGO
    String sqlSlots = "DELETE FROM parking_slot WHERE id_parking_lot = ?";
    // 3. Borrar el parqueo
    String sqlParqueo = "DELETE FROM parking_lot WHERE id = ?";

    try (Connection conn = DbConnection.getConnection()) {
        conn.setAutoCommit(false); 

        try (PreparedStatement ps1 = conn.prepareStatement(sqlAsignaciones);
             PreparedStatement ps2 = conn.prepareStatement(sqlSlots);
             PreparedStatement ps3 = conn.prepareStatement(sqlParqueo)) {

            ps1.setInt(1, id);
            ps1.executeUpdate();

            ps2.setInt(1, id);
            ps2.executeUpdate();

            ps3.setInt(1, id);
            int rows = ps3.executeUpdate();

            conn.commit(); 
            return rows > 0;

        } catch (SQLException e) {
            conn.rollback(); 
            System.err.println("Error en transacción de borrado: " + e.getMessage());
            return false;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    public boolean update(ParkingLot p) {
        String sql = "UPDATE parking_lot SET name = ?, number_of_spaces = ? WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getName());
            pstmt.setInt(2, p.getNumberOfSpaces());
            pstmt.setInt(3, p.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar parqueo: " + e.getMessage());
            return false;
        }
    }

    public ParkingLot findById(int id) {
        String sql = "SELECT * FROM parking_lot WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new ParkingLot(rs.getInt("id"), rs.getString("name"), rs.getInt("number_of_spaces"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean saveOrUpdateSlot(int lotId, int slotNumber, boolean isDisability) {

    String sql = "INSERT INTO parking_slot (id_parking_lot, slot_number, is_disability_only) " +
                 "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE is_disability_only = ?";
    try (Connection conn = DbConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, lotId);
        pstmt.setInt(2, slotNumber);
        pstmt.setBoolean(3, isDisability);
        pstmt.setBoolean(4, isDisability);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
}
