/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Kenneth
 */
public class ParkingLot {

    private int id;
    private String name;
    private int numberOfSpaces;
    private int occupiedSpaces; // Nuevo atributo

// Getter y Setter
    public int getOccupatedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupatedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }

    public ParkingLot(int id, String name, int numberOfSpaces) {
        this.id = id;
        this.name = name;
        this.numberOfSpaces = numberOfSpaces;
    }

    public ParkingLot() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfSpaces() {
        return numberOfSpaces;
    }

    public void setNumberOfSpaces(int numberOfSpaces) {
        this.numberOfSpaces = numberOfSpaces;
    }

}
