/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Kenneth
 */
public class VehicleType {

    private int idVehicleType;
    private String description;

    public VehicleType() {
    }

    public VehicleType(int idVehicleType, String description) {
        this.idVehicleType = idVehicleType;
        this.description = description;
    }

    // Getters y Setters
    public int getIdVehicleType() {
        return idVehicleType;
    }

    public void setIdVehicleType(int idVehicleType) {
        this.idVehicleType = idVehicleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
