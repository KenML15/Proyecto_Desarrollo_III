/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Kenneth
 */
public class ParkingSpace {

    private int number;
    private boolean occupied;
    private String plate;
    private boolean disability; // Nuevo atributo

    public ParkingSpace(int number, boolean occupied, String plate) {
        this.number = number;
        this.occupied = occupied;
        this.plate = plate;
    }

    public ParkingSpace() {
    }
    
    

    // Getters y Setters
    public boolean isDisability() {
        return disability;
    }

    public void setDisability(boolean disability) {
        this.disability = disability;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

}
