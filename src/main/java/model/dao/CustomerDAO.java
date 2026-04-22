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
import model.entity.Customer;

public class CustomerDAO {

    //create
    public boolean insert(Customer c) {
        // Ya no enviamos el ID, MySQL lo pone solo
        String sql = "INSERT INTO customer (name, disability) VALUES (?, ?)";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, c.getName());
            pstmt.setBoolean(2, c.isDisabilityPresented());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //read, ontener todos los clientes
    public List<Customer> findAll() {
    List<Customer> list = new ArrayList<>();
    String sql = "SELECT * FROM customer";
    try (Connection conn = DbConnection.getConnection(); 
         Statement stmt = conn.createStatement(); 
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            list.add(new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBoolean("disability")
            ));
        }
        System.out.println("DAO: Se cargaron " + list.size() + " clientes de la base de datos.");
    } catch (SQLException e) {
        System.err.println("ERROR CRÍTICO EN DAO: " + e.getMessage());
    }
    return list;
}

    //read, obtener un cliente especifico por medio del id
    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getBoolean("disability") // Cambiar aquí también
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por ID: " + e.getMessage());
        }
        return null;
    }

    //update, si presenta una discapacidad de un cliente existente
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET name = ?, disability = ? WHERE id = ?";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getName());
            pstmt.setBoolean(2, customer.isDisabilityPresented());
            pstmt.setInt(3, customer.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            return false;
        }

    }

    //delete
    public boolean delete(int id) {
        String sql = "DELETE FROM customer WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}
