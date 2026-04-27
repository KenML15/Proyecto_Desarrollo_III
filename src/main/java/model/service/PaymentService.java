/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.service;

import java.time.Duration;
import java.time.LocalDateTime;
import model.entity.Rate;

/**
 *
 * @author Jefferson
 */
public class PaymentService {

    /**
     * Calcula el monto total a pagar basándose en la estancia y las tarifas.
     */
    public float calculateFinalAmount(LocalDateTime entryDate, LocalDateTime exitDate, Rate rate, boolean hasDisability) {
        if (exitDate == null) exitDate = LocalDateTime.now();
        
        Duration duration = Duration.between(entryDate, exitDate);
        long minutes = duration.toMinutes();
        float total = 0;

        // Lógica de cobro por escalas (puedes ajustarla según el negocio)
        if (minutes <= 0) {
            total = 0;
        } else if (minutes <= 30) {
            total = rate.getHalfHour();
        } else if (minutes <= 60) {
            total = rate.getHour();
        } else if (minutes <= 1440) { // Menos de un día
            // Cobro por horas completas (redondeando hacia arriba)
            float hours = (float) Math.ceil(minutes / 60.0);
            total = hours * rate.getHour();
        } else {
            // Cobro por días
            float days = (float) Math.ceil(minutes / 1440.0);
            total = days * rate.getDay();
        }

        // Aplicar descuento por Ley 7600 (Discapacidad) si aplica
        if (hasDisability) {
            total = total * 0.5f; // 50% de descuento
        }

        return total;
    }
}