package main.negocio.servicio;

import main.negocio.dto.Entregable;
import java.time.LocalDate;
import main.comun.ExcepcionMostrableUsuario;

public class ValidadorEntrega {
    
    /**
     * Valida si un entregable puede ser subido por el estudiante.
     * @param deliverable El entregable a validar
     * @throws ExcepcionMostrableUsuario con el mensaje de error si no es válido
     */
    public static void validateDelivery(Entregable deliverable) throws ExcepcionMostrableUsuario {
        if (deliverable == null) {
            throw new ExcepcionMostrableUsuario("Selección requerida", "No hay entregable", "No hay ningún documento seleccionado.");
        }
        
        if ("Inhabilitado".equalsIgnoreCase(deliverable.getEstado())) {
            throw new ExcepcionMostrableUsuario("Entregable Inhabilitado", "Aún no es posible entregar", "Este documento aún se encuentra Inhabilitado por el Profesor.");
        }
        
        if (deliverable.getFechaLimite() != null) {
            LocalDate deadline = deliverable.getFechaLimite().toLocalDate();
            LocalDate today = LocalDate.now();
            
            // Límite: a un día más de la fecha límite
            if (today.isAfter(deadline.plusDays(1))) {
                throw new ExcepcionMostrableUsuario("Fecha Expirada", "El plazo ha concluido", "La fecha límite (" + deadline.toString() + ") ha expirado. Ya no se pueden realizar entregas.");
            }
        }
    }
}
