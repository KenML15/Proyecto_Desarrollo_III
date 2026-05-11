package model.entity;

public class Customer {

    private int id;
    private String name;
    private boolean disabilityPresented;
    private String cedula;
    private String telefono;
    private String correo;

    public Customer() {}

    public Customer(int id, String name, boolean disabilityPresented) {
        this.id = id;
        this.name = name;
        this.disabilityPresented = disabilityPresented;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDisabilityPresented() { return disabilityPresented; }
    public void setDisabilityPresented(boolean disabilityPresented) { this.disabilityPresented = disabilityPresented; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}