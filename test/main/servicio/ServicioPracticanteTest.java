package main.servicio;

import main.negocio.dto.PracticanteDTO;
import main.comun.ExcepcionMostrableUsuario;
import org.junit.Test;
import static org.junit.Assert.*;

public class ServicioPracticanteTest {

    @Test
    public void testRegistrarNuevoPracticante_CorreoInvalido() {
        PracticanteDTO practicante = new PracticanteDTO.PracticanteBuilder()
                .setNombre("Juan")
                .setApellidoPaterno("Perez")
                .setApellidoMaterno("Gomez")
                .setMatricula("S20010000")
                .setCorreo("juan@gmail.com") // Invalid email
                .setEstado("Activo")
                .build();
        
        try {
            ServicioPracticante.registrarNuevoPracticante(practicante);
            fail("Debió lanzar ExcepcionMostrableUsuario por correo inválido");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato de correo electrónico inválido", e.getHeader());
        }
    }

    @Test
    public void testRegistrarNuevoPracticante_NombreConNumeros() {
        PracticanteDTO practicante = new PracticanteDTO.PracticanteBuilder()
                .setNombre("Juan123") // Numeros en el nombre no permitidos
                .setApellidoPaterno("Perez")
                .setApellidoMaterno("Gomez")
                .setMatricula("S20010000")
                .setCorreo("zS20010000@estudiantes.uv.mx")
                .setEstado("Activo")
                .build();
        
        try {
            ServicioPracticante.registrarNuevoPracticante(practicante);
            fail("Debió lanzar ExcepcionMostrableUsuario por nombre inválido");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("El nombre y apellidos solo pueden contener letras y acentos", e.getHeader());
        }
    }

    @Test
    public void testRegistrarNuevoPracticante_MatriculaInvalida() {
        PracticanteDTO practicante = new PracticanteDTO.PracticanteBuilder()
                .setNombre("Juan")
                .setApellidoPaterno("Perez")
                .setApellidoMaterno("Gomez")
                .setMatricula("zS20010") // Matricula incorrecta, le falta longitud y S inicial
                .setCorreo("zS20010000@estudiantes.uv.mx")
                .setEstado("Activo")
                .build();
        
        try {
            ServicioPracticante.registrarNuevoPracticante(practicante);
            fail("Debió lanzar ExcepcionMostrableUsuario por matrícula inválida");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato de matrícula inválido", e.getHeader());
        }
    }

    @Test
    public void testRegistrarNuevoPracticante_CamposVacios() {
        PracticanteDTO practicante = new PracticanteDTO.PracticanteBuilder()
                .setNombre("") // Campo vacio
                .setApellidoPaterno("Perez")
                .setApellidoMaterno("Gomez")
                .setMatricula("S20010000")
                .setCorreo("zS20010000@estudiantes.uv.mx")
                .setEstado("Activo")
                .build();
        
        try {
            ServicioPracticante.registrarNuevoPracticante(practicante);
            fail("Debió lanzar ExcepcionMostrableUsuario por campos incompletos");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Campos Incompletos", e.getHeader());
        }
    }
}
