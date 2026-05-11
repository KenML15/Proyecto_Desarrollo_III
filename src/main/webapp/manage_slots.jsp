<%-- 
    Document   : manage_slots
    Created on : 9 may 2026, 6:35:42 p.m.
    Author     : Kenneth
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configuración de Espacios | Sistema de Parqueo</title>
        <link rel="stylesheet" href="CSS/style.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    </head>
    <body>
        <div class="manage-container">
            <h2>Configurar Espacios - Parqueo #${lotId}</h2>
            <p style="text-align: center; color: #4a5568;">
                Capacidad total permitida: <strong>${lot.numberOfSpaces} espacios</strong>
            </p>

            <form action="assignments?action=saveSlot" method="POST" class="manage-form">
                <input type="hidden" name="idParkingLot" value="${lotId}">

                <div class="form-group">
                    <label>Número de Espacio (Rango 1 - ${lot.numberOfSpaces}):</label>
                    <input type="number" 
                           name="slotNumber" 
                           min="1" 
                           max="${lot.numberOfSpaces}" 
                           required 
                           placeholder="Ej: 1"
                           class="form-control">
                </div>

                <div class="form-group" style="margin: 15px 0;">
                    <label class="checkbox-label" style="display: flex; align-items: center; cursor: pointer;">
                        <input type="checkbox" name="isDisability" style="margin-right: 10px; transform: scale(1.2);"> 
                        ¿Marcar como espacio para discapacidad (Ley 7600)?
                    </label>
                </div>

                <button type="submit" class="btn-save-slots" style="width: 100%; padding: 12px; cursor: pointer;">
                    Guardar Configuración de Espacio
                </button>
            </form>

            <hr style="margin: 30px 0; border: 0; border-top: 1px solid #e2e8f0;">

            <h3>Espacios configurados actualmente</h3>
            <table class="manage-table" style="width: 100%; border-collapse: collapse; margin-top: 10px;">
                <thead>
                    <tr style="background-color: #edf2f7;">
                        <th style="padding: 12px; border: 1px solid #e2e8f0;"># Espacio</th>
                        <th style="padding: 12px; border: 1px solid #e2e8f0;">Tipo / Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="s" items="${currentSlots}">
                        <tr style="text-align: center; border-bottom: 1px solid #e2e8f0;">
                            <td style="padding: 10px;"><strong>Espacio ${s.number}</strong></td>
                            <td style="padding: 10px;">
                                <span class="${s.disability ? 'status-blue' : 'status-gray'}" 
                                      style="padding: 5px 10px; border-radius: 4px; font-size: 0.9em;
                                      background-color: ${s.disability ? '#ebf8ff' : '#f7fafc'};
                                      color: ${s.disability ? '#2b6cb0' : '#4a5568'};
                                      border: 1px solid ${s.disability ? '#bee3f8' : '#e2e8f0'};">
                                    ${s.disability ? "♿ Especial (Discapacidad)" : "Estándar"}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty currentSlots}">
                        <tr>
                            <td colspan="2" style="text-align: center; padding: 20px; color: #a0aec0; font-style: italic;">
                                No hay reglas específicas configuradas. Todos los espacios se asumen como 'Estándar'.
                            </td>
                        </tr>
                    </c:if>
                </tbody>


            </table>

            <div style="margin-top: 30px; text-align: center; display: flex; flex-direction: column; gap: 15px; align-items: center;">

                <a href="assignments?action=dashboard" class="btn-table" 
                   style="text-decoration: none; background: #4a5568; color: white; padding: 10px 25px; border-radius: 5px; width: 200px;">
                    Volver al Dashboard
                </a>

                <a href="menu_admin.jsp" class="btn-table" 
                   style="text-decoration: none; background: #2d3748; color: white; padding: 10px 25px; border-radius: 5px; width: 200px; font-weight: bold;">
                    Volver al Menú Principal
                </a>

            </div>