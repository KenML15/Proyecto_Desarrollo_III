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

public class VehicleDAO {

    // Create
    public boolean insert(Vehicle v, int typeId) {
        // Usamos 'id_vehicle_type' que es el nombre real en tu tabla
        String sql = "INSERT INTO vehicle (plate, color, brand, model, id_vehicle_type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getPlate());
            ps.setString(2, v.getColor());
            ps.setString(3, v.getBrand());
            ps.setString(4, v.getModel());
            ps.setInt(5, typeId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error en insert: " + e.getMessage());
            return false;
        }
    }

    public int countOwners(String plate) {
        String sql = "SELECT COUNT(*) FROM client_vehicle WHERE plate_vehicle = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error en countOwners: " + e.getMessage());
        }
        return 0;
    }

    public boolean assignCustomer(String plate, int idCustomer) {
        String sql = "INSERT INTO client_vehicle (plate_vehicle, id_customer) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate);
            ps.setInt(2, idCustomer);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Esto te dirá si el error es por duplicado o por otra razón
            System.out.println("Error en assignCustomer: " + e.getMessage());
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
                v.setIdVehicleType(rs.getInt("id_vehicle_type"));
                v.setIdCustomer(rs.getInt("id_customer"));
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
        // Esta consulta trae los datos del vehículo y concatena los nombres de los dueños
        String sql = "SELECT v.*, GROUP_CONCAT(c.name SEPARATOR ', ') AS all_owners "
                + "FROM vehicle v "
                + "LEFT JOIN client_vehicle cv ON v.plate = cv.plate_vehicle "
                + "LEFT JOIN customer c ON cv.id_customer = c.id "
                + "GROUP BY v.plate";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("plate"));
                v.setBrand(rs.getString("brand"));
                v.setModel(rs.getString("model"));
                v.setColor(rs.getString("color"));

                // IMPORTANTE: Aquí llenamos el nuevo atributo ownerName con el resultado del JOIN
                v.setOwnerName(rs.getString("all_owners"));

                list.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos con dueños: " + e.getMessage());
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

    public boolean hasDisabilityOwner(String plate) {
        // Usando los nombres reales: tabla 'client_vehicle' y columna 'disability'
        String sql = "SELECT COUNT(*) FROM customer c "
                + "JOIN client_vehicle cv ON c.id = cv.id_customer "
                + "WHERE cv.plate_vehicle = ? AND c.disability = 1";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error en hasDisabilityOwner: " + e.getMessage());
        }
        return false;
    }

}
