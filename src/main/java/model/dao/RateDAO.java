package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.entity.Rate;


public class RateDAO {

 
    public Rate getFullRateByVehicleType(int idVehicleType) {
        String sql = "SELECT * FROM rate WHERE id_vehicle_type = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicleType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRate(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public List<Rate> findAll() {
        List<Rate> list = new ArrayList<>();
        String sql = "SELECT * FROM rate ORDER BY id_vehicle_type";
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRate(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

 
    public boolean saveOrUpdate(Rate r) {
        String sql = "INSERT INTO rate (id_vehicle_type, half_hour_fee, hour_fee, day_fee, week_fee, month_fee, year_fee) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "half_hour_fee=VALUES(half_hour_fee), hour_fee=VALUES(hour_fee), " +
                     "day_fee=VALUES(day_fee), week_fee=VALUES(week_fee), " +
                     "month_fee=VALUES(month_fee), year_fee=VALUES(year_fee)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, r.getIdVehicleType());
            pstmt.setFloat(2, r.getHalfHour());
            pstmt.setFloat(3, r.getHour());
            pstmt.setFloat(4, r.getDay());
            pstmt.setFloat(5, r.getWeek());
            pstmt.setFloat(6, r.getMonth());
            pstmt.setFloat(7, r.getYear());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Rate mapRate(ResultSet rs) throws SQLException {
        return new Rate(
            rs.getInt("id_vehicle_type"),
            rs.getFloat("half_hour_fee"),
            rs.getFloat("hour_fee"),
            rs.getFloat("day_fee"),
            rs.getFloat("week_fee"),
            rs.getFloat("month_fee"),
            rs.getFloat("year_fee")
        );
    }
}
