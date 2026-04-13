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
        String sql = "INSERT INTO parking_lot (id, name, number_of_spaces) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, p.getId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getNumberOfSpaces());

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
        String sql = "DELETE FROM parking_lot WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar parqueo: " + e.getMessage());
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
}
