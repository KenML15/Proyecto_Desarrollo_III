package model.entity;

import java.sql.Timestamp;

public class VehicleAssignment {

    private int id;
    private String plateVehicle;
    private int idParkingLot;
    private Timestamp entryTime;
    private String status;
    private String lotName;

// Agrega el getter y setter
    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public VehicleAssignment() {
    }

    public VehicleAssignment(int id, String plateVehicle, int idParkingLot, Timestamp entryTime, String status) {
        this.id = id;
        this.plateVehicle = plateVehicle;
        this.idParkingLot = idParkingLot;
        this.entryTime = entryTime;
        this.status = status;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlateVehicle() {
        return plateVehicle;
    }

    public void setPlateVehicle(String plateVehicle) {
        this.plateVehicle = plateVehicle;
    }

    public int getIdParkingLot() {
        return idParkingLot;
    }

    public void setIdParkingLot(int idParkingLot) {
        this.id = idParkingLot;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
