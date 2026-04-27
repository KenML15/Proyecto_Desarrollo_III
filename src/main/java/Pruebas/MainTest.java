/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pruebas;

/**
 *
 * @author Jefferson
 */

import java.time.LocalDateTime;
import model.entity.Rate;
import model.service.PaymentService;

public class MainTest {

    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();

        // 1. Configurar una tarifa de prueba (Simulando lo que vendría de la BD)
        // Rate(id, half, hour, day, week, month, year)
        Rate tarifaAuto = new Rate(1, 500f, 1000f, 5000f, 30000f, 100000f, 1200000f);

        // 2. Escenario A: Cliente Regular - Estancia de 45 minutos
        // Debería cobrar la hora completa (1000)
        LocalDateTime entradaA = LocalDateTime.now().minusMinutes(45);
        float cobroA = paymentService.calculateFinalAmount(entradaA, LocalDateTime.now(), tarifaAuto, false);
        
        // 3. Escenario B: Cliente con Discapacidad - Estancia de 2 horas
        // Debería cobrar 2 horas (2000) con 50% desc = 1000
        LocalDateTime entradaB = LocalDateTime.now().minusHours(2);
        float cobroB = paymentService.calculateFinalAmount(entradaB, LocalDateTime.now(), tarifaAuto, true);

        // 4. Escenario C: Estancia corta - 15 minutos
        // Debería cobrar media hora (500)
        LocalDateTime entradaC = LocalDateTime.now().minusMinutes(15);
        float cobroC = paymentService.calculateFinalAmount(entradaC, LocalDateTime.now(), tarifaAuto, false);

        // --- IMPRESIÓN DE RESULTADOS ---
        System.out.println("======= PRUEBAS DE SISTEMA DE COBRO =======");
        System.out.println("Escenario A (45 min, Regular): esperado 1000.0 -> REAL: " + cobroA);
        System.out.println("Escenario B (2 hrs, Ley 7600): esperado 1000.0 -> REAL: " + cobroB);
        System.out.println("Escenario C (15 min, Regular): esperado 500.0  -> REAL: " + cobroC);
        System.out.println("===========================================");
        
        if (cobroA == 1000f && cobroB == 1000f && cobroC == 500f) {
            System.out.println("✅ ¡TODAS LAS PRUEBAS PASARON EXITOSAMENTE!");
        } else {
            System.out.println("❌ HAY UN ERROR EN LA LÓGICA DE CÁLCULO.");
        }
    }
}