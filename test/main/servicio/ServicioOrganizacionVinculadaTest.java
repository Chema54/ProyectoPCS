package main.servicio;

import main.negocio.dto.OrganizacionVinculadaDTO;
import main.comun.ExcepcionMostrableUsuario;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServicioOrganizacionVinculadaTest {

    @Test
    public void testRegistrarNuevaOrganizacion_NombreDuplicado() {
        OrganizacionVinculadaDTO organizacion = new OrganizacionVinculadaDTO.OrganizacionBuilder()
                .setNombreEmpresa("Universidad Veracruzana")
                .setCorreo("contacto@uv.mx")
                .setTelefono("2288421700")
                .setDireccion("Xalapa, Veracruz")
                .build();
        
        try {
            // Intento 1: Podría pasar si la BD está limpia
            ServicioOrganizacionVinculada.registrarNuevaOrganizacion(organizacion);
            
            // Intento 2: Definitivamente debe fallar por ser nombre duplicado
            ServicioOrganizacionVinculada.registrarNuevaOrganizacion(organizacion);
            
            fail("Debió lanzar ExcepcionMostrableUsuario por organización duplicada en el segundo intento");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Organización Duplicada", e.getHeader());
        }
    }
}
