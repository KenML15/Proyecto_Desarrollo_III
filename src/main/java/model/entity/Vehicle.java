package model.entity;

public class Vehicle {

    private String plate;
    private String color;
    private String brand;
    private String model;
    private int idVehicleType;
    private int idCustomer;
    private String ownerName;       // populated via JOIN, not stored directly
    private String vehicleTypeDesc; // populated via JOIN, not stored directly

    public Vehicle() {}

    public Vehicle(String plate, String color, String brand, String model, int idVehicleType) {
        this.plate = plate;
        this.color = color;
        this.brand = brand;
        this.model = model;
        this.idVehicleType = idVehicleType;
    }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getIdVehicleType() { return idVehicleType; }
    public void setIdVehicleType(int idVehicleType) { this.idVehicleType = idVehicleType; }

    public int getIdCustomer() { return idCustomer; }
    public void setIdCustomer(int idCustomer) { this.idCustomer = idCustomer; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getVehicleTypeDesc() { return vehicleTypeDesc; }
    public void setVehicleTypeDesc(String vehicleTypeDesc) { this.vehicleTypeDesc = vehicleTypeDesc; }
}