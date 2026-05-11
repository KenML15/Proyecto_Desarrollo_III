package model.entity;

import java.sql.Timestamp;


public class VehicleAssignment {

    private int id;
    private String plateVehicle;
    private int idParkingLot;
    private int assignedSlot;
    private Timestamp entryTime;
    private String status;
    private String lotName;

    public VehicleAssignment() {}

    public VehicleAssignment(int id, String plateVehicle, int idParkingLot,
                              int assignedSlot, Timestamp entryTime, String status) {
        this.id = id;
        this.plateVehicle = plateVehicle;
        this.idParkingLot = idParkingLot;
        this.assignedSlot = assignedSlot;
        this.entryTime = entryTime;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPlateVehicle() { return plateVehicle; }
    public void setPlateVehicle(String plateVehicle) { this.plateVehicle = plateVehicle; }

    public int getIdParkingLot() { return idParkingLot; }
    public void setIdParkingLot(int idParkingLot) { this.idParkingLot = idParkingLot; }

    public int getAssignedSlot() { return assignedSlot; }
    public void setAssignedSlot(int assignedSlot) { this.assignedSlot = assignedSlot; }

    public Timestamp getEntryTime() { return entryTime; }
    public void setEntryTime(Timestamp entryTime) { this.entryTime = entryTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLotName() { return lotName; }
    public void setLotName(String lotName) { this.lotName = lotName; }
}
