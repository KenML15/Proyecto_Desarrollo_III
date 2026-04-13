/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Kenneth
 */
public class Vehicle {

    private String plate;
    private String color;
    private String brand;
    private String model;
    private VehicleType type;

     //constructor completo
//    public Vehicle(String plate, String color, String brand, String model, VehicleType type) {
//        this.plate = plate;
//        this.color = color;
//        this.brand = brand;
//        this.model = model;
//        this.type = type;
//    }
    
    //CONSTRUCTOR TEMPORAL
    public Vehicle(String plate, String color, String brand, String model) {
        this.plate = plate;
        this.color = color;
        this.brand = brand;
        this.model = model;
    }

    public Vehicle() {
    }

    // Getters y Setters
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}