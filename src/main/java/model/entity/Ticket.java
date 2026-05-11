/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Kenneth
 */
public class Ticket {

    private int id;
    private LocalDateTime entryDate;
    private LocalDateTime exitDate;
    private float totalAmount;
    private Customer customer;
    private Vehicle vehicle;
    private Space space;

    public Ticket(int id, LocalDateTime entryDate, LocalDateTime exitDate, float totalAmount, Customer customer, Vehicle vehicle, Space space) {
        this.id = id;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.totalAmount = totalAmount;
        this.customer = customer;
        this.vehicle = vehicle;
        this.space = space;
    }

    public String getEntryDateFormatted() {
        if (this.entryDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.entryDate.format(formatter);
    }

    public String getExitDateFormatted() {
        if (this.exitDate == null) {
            return "";
        }
        return this.exitDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /**
     * Returns a human-readable stay duration string.
     * If exitDate is null, calculates up to the current moment.
     */
    public String getStayDuration() {
        LocalDateTime end = (this.exitDate != null) ? this.exitDate : LocalDateTime.now();
        if (this.entryDate == null) return "—";
        Duration d = Duration.between(this.entryDate, end);
        long totalMinutes = d.toMinutes();
        if (totalMinutes < 1) return "Menos de 1 min";
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        if (hours == 0) return minutes + " min";
        if (minutes == 0) return hours + " h";
        return hours + " h " + minutes + " min";
    }

    public Ticket() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDateTime getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDateTime exitDate) {
        this.exitDate = exitDate;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

}