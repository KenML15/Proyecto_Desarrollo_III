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
import model.entity.Rate;

/**
 *
 * @author Jefferson
 */
public class RateDAO {

    public boolean updateRate(int idVehicleType, float newAmount) {
        String sql = "INSERT INTO rate (id_vehicle_type, amount_per_hour) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE amount_per_hour = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicleType);
            pstmt.setFloat(2, newAmount);
            pstmt.setFloat(3, newAmount);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public float getRateByVehicleType(int idVehicleType) {
        String sql = "SELECT amount_per_hour FROM rate WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicleType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("amount_per_hour");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public boolean saveOrUpdateRate(Rate rate) {
        String sql = "INSERT INTO rate (id_vehicle_type, half_hour_fee, hour_fee, day_fee, week_fee, month_fee, year_fee) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE half_hour_fee=?, hour_fee=?, day_fee=?, week_fee=?, month_fee=?, year_fee=?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, rate.getIdVehicleType());
            pstmt.setFloat(2, rate.getHalfHour());
            pstmt.setFloat(3, rate.getHour());
            pstmt.setFloat(4, rate.getDay());
            pstmt.setFloat(5, rate.getWeek());
            pstmt.setFloat(6, rate.getMonth());
            pstmt.setFloat(7, rate.getYear());

            // Parámetros para el UPDATE
            pstmt.setFloat(8, rate.getHalfHour());
            pstmt.setFloat(9, rate.getHour());
            pstmt.setFloat(10, rate.getDay());
            pstmt.setFloat(11, rate.getWeek());
            pstmt.setFloat(12, rate.getMonth());
            pstmt.setFloat(13, rate.getYear());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Object[]> listRates() {
    List<Object[]> data = new ArrayList<>();
    String sql = "SELECT vt.description, r.half_hour_fee, r.hour_fee, r.day_fee, r.week_fee, r.month_fee, r.year_fee " +
                 "FROM rate r " +
                 "JOIN vehicle_type vt ON r.id_vehicle_type = vt.id_vehicle_type";
    
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        while (rs.next()) {
            Object[] row = {
                rs.getString("description"),
                "₡" + rs.getFloat("half_hour_fee"),
                "₡" + rs.getFloat("hour_fee"),
                "₡" + rs.getFloat("day_fee"),
                "₡" + rs.getFloat("week_fee"),
                "₡" + rs.getFloat("month_fee"),
                "₡" + rs.getFloat("year_fee")
            };
            data.add(row);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar: " + e.getMessage());
    }
    return data;
}
}
