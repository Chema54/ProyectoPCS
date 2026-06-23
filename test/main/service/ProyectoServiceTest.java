package main.service;

import main.business.dto.ProyectoDTO;
import main.business.dto.TitularProyectoDTO;
import main.common.UserDisplayableException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProyectoServiceTest {

    @Test
    public void testRegisterNewProject_NegativeCapacity() {
        ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                .setName("Desarrollo de Software")
                .setTotalCapacity(-5) // Invalido, no puede ser negativo
                .setAvailableSpaces(-5)
                .setStatus("Sin asignar")
                .build();
                
        TitularProyectoDTO titular = new TitularProyectoDTO.TitularBuilder()
                .setName("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizationId(1)
                .build();
        
        try {
            ProyectoService.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar UserDisplayableException por cupo inválido");
        } catch (UserDisplayableException e) {
            assertEquals("Cupo Total Inválido", e.getHeader());
        }
    }
    
    @Test
    public void testRegisterNewProject_ZeroCapacity() {
        ProyectoDTO proyecto = new ProyectoDTO.ProyectoBuilder()
                .setName("Desarrollo de IA")
                .setTotalCapacity(0) // Invalido, debe ser mayor a 0
                .setAvailableSpaces(0)
                .setStatus("Sin asignar")
                .build();
                
        TitularProyectoDTO titular = new TitularProyectoDTO.TitularBuilder()
                .setName("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizationId(1)
                .build();
        
        try {
            ProyectoService.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar UserDisplayableException por cupo inválido");
        } catch (UserDisplayableException e) {
            assertEquals("Cupo Total Inválido", e.getHeader());
        }
    }
}
