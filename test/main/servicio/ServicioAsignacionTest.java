package main.servicio;

import main.negocio.dto.AsignacionDTO;
import main.comun.ExcepcionMostrableUsuario;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServicioAsignacionTest {

    @Test
    public void testRegistrarNuevaAsignacion_DatosIncompletos() {
        AsignacionDTO asignacion = new AsignacionDTO.AsignacionBuilder()
                .setPracticanteId(0) // ID invalido
                .setProyectoId(0) // ID invalido
                .setExperienciaEducativaId(1)
                .setEstado("Asignado")
                .build();
        
        try {
            ServicioAsignacion.registerNewAssignment(asignacion);
            fail("Debió lanzar ExcepcionMostrableUsuario por IDs inválidos o vacíos");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Campos Incompletos", e.getHeader());
        }
    }
    
    @Test
    public void testRegistrarNuevaAsignacion_SinPracticante() {
        AsignacionDTO asignacion = new AsignacionDTO.AsignacionBuilder()
                .setPracticanteId(-1) // ID invalido
                .setProyectoId(5) 
                .setExperienciaEducativaId(1)
                .setEstado("Asignado")
                .build();
        
        try {
            ServicioAsignacion.registerNewAssignment(asignacion);
            fail("Debió lanzar ExcepcionMostrableUsuario por falta de practicante");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Campos Incompletos", e.getHeader());
        }
    }
}
