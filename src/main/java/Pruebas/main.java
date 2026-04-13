/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

import model.dao.CustomerDAO;
import model.dao.SpaceDAO;
import model.dao.TicketDAO;
import model.dao.VehicleDAO;
import model.entity.Customer;
import model.entity.Vehicle;

/**
 *
 * @author Kenneth
 */
public class main {
    
   public static void main(String[] args) {
        System.out.println("--- INICIANDO PRUEBAS DE INSERCIÓN ---");

        // 1. Probar Cliente
        CustomerDAO customerDAO = new CustomerDAO();
        Customer cliente = new Customer(102, "Kenneth Lopez", true);
        if (customerDAO.insert(cliente)) {
            System.out.println("Cliente insertado.");
        }

        // 2. Probar Tipo de Vehículo (Si ya insertaste por SQL, busquemos el ID 1)
        // Usaremos el ID 2 (Automóvil) que insertamos en el script anterior
        int idMoto = 1; 

        // 3. Probar Vehículo
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle carro = new Vehicle();
        carro.setPlate("ABC-123");
        carro.setColor("Azul");
        carro.setBrand("Toyota");
        carro.setModel("Corolla");
        
        if (vehicleDAO.insert(carro, idMoto)) {
            System.out.println("Vehículo insertado y asociado al tipo.");
        }

        // 4. Probar Espacio (Ocupar el espacio 1)
        SpaceDAO spaceDAO = new SpaceDAO();
        if (spaceDAO.updateStatus(1, true)) {
            System.out.println("Espacio #1 marcado como OCUPADO.");
        }

        // 5. Probar Ticket (Entrada)
        TicketDAO ticketDAO = new TicketDAO();
        if (ticketDAO.openTicket(101, "ABC-123", 1)) {
            System.out.println("Ticket de entrada CREADO exitosamente.");
        }

        System.out.println("\n--- PRUEBA FINALIZADA: Revisa tu phpMyAdmin ---");
    }
    
}
