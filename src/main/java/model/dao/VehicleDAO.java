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

public class VehicleDAO {

    public boolean insert(Vehicle vehicle, int idVehicleType) {
        String sql = "INSERT INTO vehicle (plate, color, brand, model, id_vehicle_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getPlate());
            pstmt.setString(2, vehicle.getColor());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, idVehicleType);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error en VehicleDAO.insert: " + e.getMessage());
            return false;
        }
    }

    public Vehicle findByPlate(String plate) {
        String sql = "SELECT * FROM vehicle WHERE plate = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getString("plate"),
                        rs.getString("color"),
                        rs.getString("brand"),
                        rs.getString("model")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        try (Connection conn = DbConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setColor(rs.getString("color"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                list.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error en VehicleDAO: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(String plate) {
        String sql = "DELETE FROM vehicle WHERE plate = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean update(Vehicle v, int idVehicleType) {

        String sql = "UPDATE vehicle SET color = ?, brand = ?, model = ?, id_vehicle_type = ? WHERE plate = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, v.getColor());
            pstmt.setString(2, v.getBrand());
            pstmt.setString(3, v.getModel());
            pstmt.setInt(4, idVehicleType);
            pstmt.setString(5, v.getPlate());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar vehículo: " + e.getMessage());
            return false;
        }
    }
}
