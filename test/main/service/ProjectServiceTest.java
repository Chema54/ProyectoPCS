package main.service;

import main.business.dto.ProjectDTO;
import main.business.dto.ProjectManagerDTO;
import main.common.UserDisplayableException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProjectServiceTest {

    @Test
    public void testRegisterNewProject_NegativeCapacity() {
        ProjectDTO proyecto = new ProjectDTO.ProjectBuilder()
                .setName("Desarrollo de Software")
                .setTotalCapacity(-5) // Invalido, no puede ser negativo
                .setAvailableSpaces(-5)
                .setStatus("Sin asignar")
                .build();
                
        ProjectManagerDTO titular = new ProjectManagerDTO.TitularBuilder()
                .setName("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizationId(1)
                .build();
        
        try {
            ProjectService.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar UserDisplayableException por cupo inválido");
        } catch (UserDisplayableException e) {
            assertEquals("Cupo Total Inválido", e.getHeader());
        }
    }
    
    @Test
    public void testRegisterNewProject_ZeroCapacity() {
        ProjectDTO proyecto = new ProjectDTO.ProjectBuilder()
                .setName("Desarrollo de IA")
                .setTotalCapacity(0) // Invalido, debe ser mayor a 0
                .setAvailableSpaces(0)
                .setStatus("Sin asignar")
                .build();
                
        ProjectManagerDTO titular = new ProjectManagerDTO.TitularBuilder()
                .setName("Profesor Test")
                .setNumeroPersonal("12345")
                .setOrganizationId(1)
                .build();
        
        try {
            ProjectService.registrarNuevoProyecto(proyecto, titular);
            fail("Debió lanzar UserDisplayableException por cupo inválido");
        } catch (UserDisplayableException e) {
            assertEquals("Cupo Total Inválido", e.getHeader());
        }
    }
}
