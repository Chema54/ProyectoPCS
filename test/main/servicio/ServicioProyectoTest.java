package main.servicio;

import main.negocio.dto.ProyectoDTO;
import main.negocio.dto.ResponsableProyectoDTO;
import main.comun.ExcepcionMostrableUsuario;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServicioProyectoTest {

    @Test
    public void testRegistrarNuevoProyecto_CupoNegativo() {
        ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                .setNombre("Desarrollo de Software")
                .setCupoTotal(-5) // Invalido, no puede ser negativo
                .setEspaciosDisponibles(-5)
                .setEstado("Sin asignar")
                .build();
                
        ResponsableProyectoDTO titular = new ResponsableProyectoDTO.TitularBuilder()
                .setNombre("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizacionId(1)
                .build();
        
        try {
            ServicioProyecto.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar ExcepcionMostrableUsuario por cupo inválido");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato Inválido", e.getHeader());
        }
    }
    
    @Test
    public void testRegistrarNuevoProyecto_CupoCero() {
        ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                .setNombre("Desarrollo de IA")
                .setCupoTotal(0) // Invalido, debe ser mayor a 0
                .setEspaciosDisponibles(0)
                .setEstado("Sin asignar")
                .build();
                
        ResponsableProyectoDTO titular = new ResponsableProyectoDTO.TitularBuilder()
                .setNombre("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizacionId(1)
                .build();
        
        try {
            ServicioProyecto.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar ExcepcionMostrableUsuario por cupo inválido");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato Inválido", e.getHeader());
        }
    }

    @Test
    public void testRegistrarNuevoProyecto_NombreVacio() {
        ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                .setNombre("") // Nombre vacio
                .setCupoTotal(5)
                .setEspaciosDisponibles(5)
                .setEstado("Sin asignar")
                .build();
                
        ResponsableProyectoDTO titular = new ResponsableProyectoDTO.TitularBuilder()
                .setNombre("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizacionId(1)
                .build();
        
        try {
            ServicioProyecto.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar ExcepcionMostrableUsuario por nombre de proyecto vacío");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Campos Incompletos", e.getHeader());
        }
    }
}
