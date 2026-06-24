package main.negocio.servicio;

import main.negocio.dto.Entregable;
import java.time.LocalDate;

public class ValidadorEntrega {
    
    /**
     * Valida si un entregable puede ser subido por el estudiante.
     * @param deliverable El entregable a validar
     * @throws Exception con el mensaje de error si no es válido
     */
    public static void validateDelivery(Entregable deliverable) throws Exception {
        if (deliverable == null) {
            throw new Exception("No hay ningún documento seleccionado.");
        }
        
        if ("Inhabilitado".equalsIgnoreCase(deliverable.getEstado())) {
            throw new Exception("Este documento aún se encuentra Inhabilitado por el Profesor.");
        }
        
        if (deliverable.getFechaLimite() != null) {
            LocalDate deadline = deliverable.getFechaLimite().toLocalDate();
            LocalDate today = LocalDate.now();
            
            // Límite: a un día más de la fecha límite
            if (today.isAfter(deadline.plusDays(1))) {
                throw new Exception("La fecha límite (" + deadline.toString() + ") ha expirado. Ya no se pueden realizar entregas.");
            }
        }
    }
}
