package model.entity;

public class ParkingSpace {

    private int number;
    private boolean occupied;
    private String plate;
    private boolean disability;

    public ParkingSpace() {}

    public ParkingSpace(int number, boolean occupied, String plate) {
        this.number = number;
        this.occupied = occupied;
        this.plate = plate;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public boolean isDisability() { return disability; }
    public void setDisability(boolean disability) { this.disability = disability; }
}