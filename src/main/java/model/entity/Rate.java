package model.entity;

public class Rate {

    private int idVehicleType;
    private float halfHour;
    private float hour;
    private float day;
    private float week;
    private float month;
    private float year;

    public Rate() {}

    public Rate(int idVehicleType, float halfHour, float hour, float day, float week, float month, float year) {
        this.idVehicleType = idVehicleType;
        this.halfHour = halfHour;
        this.hour = hour;
        this.day = day;
        this.week = week;
        this.month = month;
        this.year = year;
    }

    public int getIdVehicleType() { return idVehicleType; }
    public void setIdVehicleType(int idVehicleType) { this.idVehicleType = idVehicleType; }

    public float getHalfHour() { return halfHour; }
    public void setHalfHour(float halfHour) { this.halfHour = halfHour; }

    public float getHour() { return hour; }
    public void setHour(float hour) { this.hour = hour; }

    public float getDay() { return day; }
    public void setDay(float day) { this.day = day; }

    public float getWeek() { return week; }
    public void setWeek(float week) { this.week = week; }

    public float getMonth() { return month; }
    public void setMonth(float month) { this.month = month; }

    public float getYear() { return year; }
    public void setYear(float year) { this.year = year; }
}