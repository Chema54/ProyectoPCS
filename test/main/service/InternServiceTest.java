package main.service;

import main.business.dto.InternDTO;
import main.common.UserDisplayableException;
import org.junit.Test;
import static org.junit.Assert.*;

public class InternServiceTest {

    @Test
    public void testRegisterNewIntern_InvalidEmail() {
        InternDTO practicante = new InternDTO.InternBuilder()
                .setName("Juan")
                .setPaternalSurname("Perez")
                .setMaternalSurname("Gomez")
                .setEnrollment("S20010000")
                .setEmail("juan@gmail.com") // Invalid email
                .setStatus("Activo")
                .build();
        
        try {
            InternService.registrarNuevoPracticante(practicante);
            fail("Debió lanzar UserDisplayableException por correo inválido");
        } catch (UserDisplayableException e) {
            assertEquals("Formato de correo electrónico inválido", e.getHeader());
        }
    }

    @Test
    public void testRegisterNewIntern_InvalidNameWithNumbers() {
        InternDTO practicante = new InternDTO.InternBuilder()
                .setName("Juan123") // Numeros en el nombre no permitidos
                .setPaternalSurname("Perez")
                .setMaternalSurname("Gomez")
                .setEnrollment("S20010000")
                .setEmail("zS20010000@estudiantes.uv.mx")
                .setStatus("Activo")
                .build();
        
        try {
            InternService.registrarNuevoPracticante(practicante);
            fail("Debió lanzar UserDisplayableException por nombre inválido");
        } catch (UserDisplayableException e) {
            assertEquals("El nombre y apellidos solo pueden contener letras y acentos", e.getHeader());
        }
    }

    @Test
    public void testRegisterNewIntern_InvalidEnrollment() {
        InternDTO practicante = new InternDTO.InternBuilder()
                .setName("Juan")
                .setPaternalSurname("Perez")
                .setMaternalSurname("Gomez")
                .setEnrollment("zS20010") // Matricula incorrecta, le falta longitud y S inicial
                .setEmail("zS20010000@estudiantes.uv.mx")
                .setStatus("Activo")
                .build();
        
        try {
            InternService.registrarNuevoPracticante(practicante);
            fail("Debió lanzar UserDisplayableException por matrícula inválida");
        } catch (UserDisplayableException e) {
            assertEquals("Formato de matrícula inválido", e.getHeader());
        }
    }
}
