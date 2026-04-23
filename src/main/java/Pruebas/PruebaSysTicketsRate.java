package Pruebas;

import model.dao.RateDAO;
import model.dao.TicketDAO;

/**
 *
 * @author Jefferson
 */
public class PruebaSysTicketsRate {

    public static void main(String[] args) {
        TicketDAO ticketDao = new TicketDAO();
        RateDAO rateDao = new RateDAO();

        System.out.println("--- INICIANDO PRUEBAS DEL SISTEMA ---");

        // PRUEBA 1: Simular datos de entrada
        float tarifaHora = 1000; 
        String placaPrueba = "123";  // Asegúrate que esta placa exista en la tabla vehicle
        int idClientePrueba = 1;     // Asegúrate que este ID exista en la tabla customer

        System.out.println("1. Intentando registrar entrada...");
        
        // MODIFICACIÓN: Capturamos el ID (int) en lugar de un boolean
        int idTicketGenerado = ticketDao.openTicket(idClientePrueba, placaPrueba);

        if (idTicketGenerado != -1) {
            System.out.println("✅ Entrada registrada exitosamente.");
            System.out.println("ID del Ticket generado: " + idTicketGenerado);

            // PRUEBA 2: Simular cálculo de cobro
            System.out.println("2. Simulando cálculo de cobro (3 horas)...");
            float totalCobro = 3 * tarifaHora; 
            System.out.println("Monto a cobrar calculado: Colones " + totalCobro);

            // PRUEBA 3: Simular salida usando el ID REAL que nos dio la DB
            System.out.println("3. Intentando registrar salida para el ticket #" + idTicketGenerado);
            boolean salidaOk = ticketDao.closeTicket(idTicketGenerado, totalCobro);
            
            if (salidaOk) {
                System.out.println("✅ Salida registrada. Ticket cerrado y pagado.");
            } else {
                System.out.println("❌ Error en salida al intentar cerrar el ticket #" + idTicketGenerado);
            }
            
        } else {
            System.out.println("❌ Error en entrada. Revisa la consola para ver el error de SQL.");
        }
        
        System.out.println("--- FIN DE LAS PRUEBAS ---");
    }
}