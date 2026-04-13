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

    //create
    public boolean insert(Vehicle vehicle, int idVehicleType) {
        String sql = "INSERT INTO vehicle (plate, color, brand, model, id_vehicle_type) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, vehicle.getPlate());
            pstmt.setString(2, vehicle.getColor());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setInt(5, idVehicleType); // Pasamos el ID del tipo
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al insertar vehículo: " + e.getMessage());
            return false;
        }
    }

    //read
    public List<Vehicle> findAll() {
        List<Vehicle> list = new ArrayList<>();
        
        String sql = "SELECT v.*, vt.description, vt.fee FROM vehicle v " +
                     "JOIN vehicle_type vt ON v.id_vehicle_type = vt.id";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setColor(rs.getString("color"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                
                // VehicleType type = new VehicleType(rs.getInt("id_vehicle_type"), rs.getString("description"), 0, rs.getFloat("fee"));
                // v.setType(type);
                
                list.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos: " + e.getMessage());
        }
        return list;
    }

    //update
    public void update(Vehicle vehicle) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
