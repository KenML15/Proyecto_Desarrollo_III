package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.ParkingLot;
import model.entity.VehicleAssignment;

public class AssignmentDAO {

    // Verifica cuántos vehículos hay con estatus 'ACTIVE' en un parqueo
    public boolean hasCapacity(int idParkingLot) {
        String sqlCount = "SELECT COUNT(*) FROM vehicle_assignment WHERE id_parking_lot = ? AND status = 'ACTIVE'";
        String sqlMax = "SELECT number_of_spaces FROM parking_lot WHERE id = ?";

        try (Connection conn = DbConnection.getConnection()) {
            int currentOccupancy = 0;
            int maxCapacity = 0;

            // Obtener ocupación actual
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCount)) {
                pstmt.setInt(1, idParkingLot);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentOccupancy = rs.getInt(1);
                }
            }

            // Obtener capacidad máxima
            try (PreparedStatement pstmt = conn.prepareStatement(sqlMax)) {
                pstmt.setInt(1, idParkingLot);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    maxCapacity = rs.getInt(1);
                }
            }

            return currentOccupancy < maxCapacity;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insert(String plate, int idLot) {
        String sql = "INSERT INTO vehicle_assignment (plate_vehicle, id_parking_lot, status) VALUES (?, ?, 'ACTIVE')";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            pstmt.setInt(2, idLot);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<VehicleAssignment> findActiveAssignments() {
        List<VehicleAssignment> list = new ArrayList<>();
        // Usamos UPPER y TRIM para que sea imposible que falle por espacios o minúsculas
        String sql = "SELECT va.id, va.plate_vehicle, va.id_parking_lot, va.entry_time, va.status, p.name AS lot_name "
                + "FROM vehicle_assignment va "
                + "JOIN parking_lot p ON va.id_parking_lot = p.id "
                + "WHERE UPPER(TRIM(va.status)) = 'ACTIVE' "
                + "ORDER BY va.entry_time DESC";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                VehicleAssignment va  = new VehicleAssignment();
                va.setId(rs.getInt("id"));
                va.setPlateVehicle(rs.getString("plate_vehicle"));
                va.setLotName(rs.getString("lot_name"));
                va.setEntryTime(rs.getTimestamp("entry_time"));
                va.setStatus(rs.getString("status").trim());
                list.add(va);
            }
            // Este log es tu brújula: si aquí dice 1, el error está fuera del DAO
            System.out.println(">>> DAO_FINAL: Enviando exactamente " + list.size() + " registros ACTIVOS.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean releaseVehicle(int assignmentId) {
        // Solo actualiza si el registro está ACTIVE
        String sql = "UPDATE vehicle_assignment SET status = 'INACTIVE' WHERE id = ? AND status = 'ACTIVE'";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, assignmentId);
            int rows = pstmt.executeUpdate();
            return rows > 0; // Devolverá falso si intentas sacar un carro que ya salió
        } catch (SQLException e) {
            return false;
        }
    }

    public List<ParkingLot> getOccupancyReport() {
        List<ParkingLot> report = new ArrayList<>();
        // Contamos SOLO los 'ACTIVE' ignorando espacios
        String sql = "SELECT p.id, p.name, p.number_of_spaces, "
                + "(SELECT COUNT(*) FROM vehicle_assignment va "
                + " WHERE va.id_parking_lot = p.id "
                + " AND UPPER(TRIM(va.status)) = 'ACTIVE') as occupied "
                + "FROM parking_lot p";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ParkingLot p = new ParkingLot();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setNumberOfSpaces(rs.getInt("number_of_spaces"));
                p.setOccupatedSpaces(rs.getInt("occupied"));
                report.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report;
    }

    public boolean releaseByPlate(String plate) {
        // Liberamos el carro activo que tenga esa placa
        String sql = "UPDATE vehicle_assignment SET status = 'INACTIVE' WHERE plate_vehicle = ? AND status = 'ACTIVE'";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
