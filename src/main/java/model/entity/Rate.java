/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author Jefferson
 */

public class Rate {
    private int idVehicleType;
    private float halfHour, hour, day, week, month, year;

    public Rate(int idVehicleType, float halfHour, float hour, float day, float week, float month, float year) {
        this.idVehicleType = idVehicleType;
        this.halfHour = halfHour;
        this.hour = hour;
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public Rate() {
    }

    // --- GETTERS CORREGIDOS ---

    public int getIdVehicleType() {
        return idVehicleType;
    }

    public float getHalfHour() {
        return halfHour;
    }

    public float getHour() {
        return hour;
    }

    public float getDay() {
        return day;
    }

    public float getWeek() {
        return week;
    }

    public float getMonth() {
        return month;
    }

    public float getYear() {
        return year;
    }

    // --- SETTERS (Necesarios para el manejo de datos) ---

    public void setIdVehicleType(int idVehicleType) {
        this.idVehicleType = idVehicleType;
    }

    public void setHalfHour(float halfHour) {
        this.halfHour = halfHour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public void setWeek(float week) {
        this.week = week;
    }

    public void setMonth(float month) {
        this.month = month;
    }

    public void setYear(float year) {
        this.year = year;
    }
}