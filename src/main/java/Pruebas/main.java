/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

import model.dao.CustomerDAO;
import model.entity.Customer;

/**
 *
 * @author Kenneth
 */
public class main {
    
    public static void main(String[] args) {
    Customer nuevo = new Customer(1, "Kenneth", true);
    CustomerDAO dao = new CustomerDAO();
    
    if(dao.insert(nuevo)) {
        System.out.println("¡Funciona! Revisa tu phpMyAdmin.");
    } else {
        System.out.println("Algo falló.");
    }
}
    
}
