package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.entity.ParkingLot;
import model.entity.ParkingSpace;
import model.entity.VehicleAssignment;

public class AssignmentDAO {

    public boolean hasCapacity(int idParkingLot) {
        String sqlCount = "SELECT COUNT(*) FROM vehicle_assignment WHERE id_parking_lot = ? AND status = 'ACTIVE'";
        String sqlMax = "SELECT number_of_spaces FROM parking_lot WHERE id = ?";

        try (Connection conn = DbConnection.getConnection()) {
            int currentOccupancy = 0;
            int maxCapacity = 0;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlCount)) {
                pstmt.setInt(1, idParkingLot);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    currentOccupancy = rs.getInt(1);
                }
            }

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

    public boolean insert(String plate, int idLot, int slotNumber) {
        String sql = "INSERT INTO vehicle_assignment (plate_vehicle, id_parking_lot, assigned_slot, entry_time, status) VALUES (?, ?, ?, NOW(), 'ACTIVE')";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate);
            ps.setInt(2, idLot);
            ps.setInt(3, slotNumber);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar asignación: " + e.getMessage());
            return false;
        }
    }

    public List<VehicleAssignment> findActiveAssignments() {
        List<VehicleAssignment> list = new ArrayList<>();
        String sql = "SELECT va.id, va.plate_vehicle, va.id_parking_lot, va.assigned_slot, va.entry_time, va.status, p.name AS lot_name "
                + "FROM vehicle_assignment va "
                + "JOIN parking_lot p ON va.id_parking_lot = p.id "
                + "WHERE UPPER(TRIM(va.status)) = 'ACTIVE' "
                + "ORDER BY va.entry_time DESC";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                VehicleAssignment va = new VehicleAssignment();
                va.setId(rs.getInt("id"));
                va.setPlateVehicle(rs.getString("plate_vehicle"));
                va.setLotName(rs.getString("lot_name"));
                va.setAssignedSlot(rs.getInt("assigned_slot"));
                va.setEntryTime(rs.getTimestamp("entry_time"));
                va.setStatus(rs.getString("status").trim());
                list.add(va);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void releaseVehicle(int id) {
        String sql = "UPDATE vehicle_assignment SET status = 'INACTIVE' WHERE id = ?";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al liberar vehículo: " + e.getMessage());
        }
    }

    public List<ParkingLot> getOccupancyReport() {
        List<ParkingLot> report = new ArrayList<>();
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

    //-------------------------------------------Tablero Parqueo-----------------------------------------------------
    public List<ParkingSpace> getBoardByParkingLot(int lotId) {
        List<ParkingSpace> spaces = new ArrayList<>();
        String sql = "SELECT s.slot_number, s.is_disability_only, va.plate_vehicle "
                + "FROM parking_slot s "
                + "LEFT JOIN vehicle_assignment va ON s.id_parking_lot = va.id_parking_lot "
                + "AND s.slot_number = va.assigned_slot AND va.status = 'ACTIVE' "
                + "WHERE s.id_parking_lot = ? "
                + "ORDER BY s.slot_number ASC";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, lotId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int number = rs.getInt("slot_number");
                    boolean isDisability = rs.getBoolean("is_disability_only");
                    String plate = rs.getString("plate_vehicle");
                    boolean isOccupied = (plate != null);

                    ParkingSpace space = new ParkingSpace(number, isOccupied, plate);
                    space.setDisability(isDisability);
                    spaces.add(space);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spaces;
    }

    public boolean isVehicleAlreadyParked(String plate) {
        String sql = "SELECT COUNT(*) FROM vehicle_assignment WHERE plate_vehicle = ? AND status = 'ACTIVE'";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, plate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public VehicleAssignment findActiveAssignmentByPlate(String plate) {
        String sql = "SELECT va.id, va.plate_vehicle, va.id_parking_lot, va.assigned_slot, " +
                     "va.entry_time, va.status, p.name AS lot_name " +
                     "FROM vehicle_assignment va " +
                     "JOIN parking_lot p ON va.id_parking_lot = p.id " +
                     "WHERE va.plate_vehicle = ? AND UPPER(TRIM(va.status)) = 'ACTIVE' LIMIT 1";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                VehicleAssignment va = new VehicleAssignment();
                va.setId(rs.getInt("id"));
                va.setPlateVehicle(rs.getString("plate_vehicle"));
                va.setIdParkingLot(rs.getInt("id_parking_lot"));
                va.setAssignedSlot(rs.getInt("assigned_slot"));
                va.setEntryTime(rs.getTimestamp("entry_time"));
                va.setStatus(rs.getString("status").trim());
                va.setLotName(rs.getString("lot_name"));
                return va;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int findFirstAvailableSlot(int idLot, boolean isDisabilityUser) {
        int slotNumber = -1;
        // Si es usuario con discapacidad, busca primero espacios de discapacidad, sino, busca espacios generales.
        String sql = isDisabilityUser
                ? "SELECT slot_number FROM parking_slot WHERE id_parking_lot = ? AND is_occupied = 0 ORDER BY is_disability_only DESC, slot_number ASC LIMIT 1"
                : "SELECT slot_number FROM parking_slot WHERE id_parking_lot = ? AND is_occupied = 0 AND is_disability_only = 0 ORDER BY slot_number ASC LIMIT 1";

        try (Connection conn = DbConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idLot);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                slotNumber = rs.getInt("slot_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slotNumber;
    }

    public boolean deactivateAssignmentByPlate(String plate) {
        String sql = "UPDATE vehicle_assignment SET status = 'INACTIVE' WHERE plate_vehicle = ? AND UPPER(TRIM(status)) = 'ACTIVE'";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the most recent assignment (active or inactive) for the given plate.
     * Used after checkout to retrieve lot/slot info for the receipt.
     */
    public VehicleAssignment findLastAssignmentByPlate(String plate) {
        String sql = "SELECT va.id, va.plate_vehicle, va.id_parking_lot, va.assigned_slot, " +
                     "va.entry_time, va.status, p.name AS lot_name " +
                     "FROM vehicle_assignment va " +
                     "JOIN parking_lot p ON va.id_parking_lot = p.id " +
                     "WHERE va.plate_vehicle = ? " +
                     "ORDER BY va.entry_time DESC LIMIT 1";
        try (Connection conn = DbConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                VehicleAssignment va = new VehicleAssignment();
                va.setId(rs.getInt("id"));
                va.setPlateVehicle(rs.getString("plate_vehicle"));
                va.setIdParkingLot(rs.getInt("id_parking_lot"));
                va.setAssignedSlot(rs.getInt("assigned_slot"));
                va.setEntryTime(rs.getTimestamp("entry_time"));
                va.setStatus(rs.getString("status").trim());
                va.setLotName(rs.getString("lot_name"));
                return va;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}