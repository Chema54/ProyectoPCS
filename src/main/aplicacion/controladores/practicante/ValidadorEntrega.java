package main.aplicacion.controladores.practicante;

import main.negocio.dto.Entregable;
import java.time.LocalDate;
import main.comun.ExcepcionMostrableUsuario;

public class ValidadorEntrega {

    public static void validateDelivery(Entregable deliverable) throws ExcepcionMostrableUsuario {
        if (deliverable == null) {
            throw new ExcepcionMostrableUsuario("Selección requerida", "No hay entregable", "No hay ningún documento seleccionado.");
        }
        
        if ("Inhabilitado".equalsIgnoreCase(deliverable.getEstado())) {
            throw new ExcepcionMostrableUsuario("Entregable Inhabilitado", "Aún no es posible entregar", "Este documento aún se encuentra Inhabilitado por el Profesor.");
        }
        
        if (deliverable.getFechaLimite() != null) {
            LocalDate expiracion = deliverable.getFechaLimite().toLocalDate();
            LocalDate hoy = LocalDate.now();
            if (hoy.isAfter(expiracion.plusDays(1))) {
                throw new ExcepcionMostrableUsuario("Fecha Expirada", "El plazo ha concluido", "La fecha límite (" + expiracion.toString() + ") ha expirado. Ya no se pueden realizar entregas.");
            }
        }
    }
}
