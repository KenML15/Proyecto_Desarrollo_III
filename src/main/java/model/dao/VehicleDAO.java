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

    // Create
public boolean insert(Vehicle v, int idType, int idCustomer) {
    // Incluimos id_customer en el INSERT
    String sql = "INSERT INTO vehicle (plate, color, brand, model, id_vehicle_type, id_customer) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conn = DbConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, v.getPlate());
        pstmt.setString(2, v.getColor());
        pstmt.setString(3, v.getBrand());
        pstmt.setString(4, v.getModel());
        pstmt.setInt(5, idType);
        
        // Si no hay cliente seleccionado (id 0), enviamos NULL a la base de datos
        if (idCustomer > 0) {
            pstmt.setInt(6, idCustomer);
        } else {
            pstmt.setNull(6, java.sql.Types.INTEGER);
        }

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al insertar vehículo con cliente: " + e.getMessage());
        return false;
    }
}

    public boolean assignCustomer(String plate, int idCustomer) {
    String sql = "UPDATE vehicle SET id_customer = ? WHERE plate = ?";
    try (Connection conn = DbConnection.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, idCustomer);
        pstmt.setString(2, plate);
        
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("Error al asignar cliente: " + e.getMessage());
        return false;
    }
}
    
    // Read por Placa
    public Vehicle findByPlate(String plate) {
        String sql = "SELECT * FROM vehicle WHERE plate = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setColor(rs.getString("color"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                // IMPORTANTE: Carga el valor de la base de datos al objeto
                v.setIdVehicleType(rs.getInt("id_vehicle_type"));
                return v;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Read Todos
 public List<Vehicle> findAll() {
    List<Vehicle> list = new ArrayList<>();
    // Usamos LEFT JOIN para que se vean los vehículos aunque no tengan dueño asignado
    String sql = "SELECT v.*, c.name as owner_name " +
                 "FROM vehicle v " +
                 "LEFT JOIN customer c ON v.id_customer = c.id";
                 
    try (Connection conn = DbConnection.getConnection(); 
         Statement stmt = conn.createStatement(); 
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Vehicle v = new Vehicle();
            v.setPlate(rs.getString("plate"));
            v.setColor(rs.getString("color"));
            v.setBrand(rs.getString("brand"));
            v.setModel(rs.getString("model"));
            v.setIdVehicleType(rs.getInt("id_vehicle_type"));
            v.setIdCustomer(rs.getInt("id_customer"));
            
            // OPCIONAL: Si quieres guardar el nombre en el objeto Vehicle, 
            // asegúrate de tener un atributo String ownerName en la clase Vehicle.
            v.setOwnerName(rs.getString("owner_name")); 
            
            list.add(v);
        }
    } catch (SQLException e) {
        System.err.println("Error en VehicleDAO.findAll: " + e.getMessage());
    }
    return list;
}

    // Delete
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

    // Update
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
