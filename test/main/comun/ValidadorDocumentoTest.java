package main.comun;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class ValidadorDocumentoTest {

    @Test
    public void testValidarArchivoPDF_Valido() {
        File file = new File("documento_aceptacion.pdf");
        try {
            ValidadorDocumento.validarArchivoPDF(file);
            // Si no lanza excepcion, la prueba pasa
        } catch (ExcepcionMostrableUsuario e) {
            fail("No debió lanzar excepción para un archivo PDF válido");
        }
    }

    @Test
    public void testValidarArchivoPDF_InvalidoWord() {
        File file = new File("documento_aceptacion.docx");
        try {
            ValidadorDocumento.validarArchivoPDF(file);
            fail("Debió lanzar excepción por formato incorrecto");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato incorrecto", e.getHeader());
        }
    }
    
    @Test
    public void testValidarArchivoPDF_InvalidoImagen() {
        File file = new File("foto.png");
        try {
            ValidadorDocumento.validarArchivoPDF(file);
            fail("Debió lanzar excepción por formato incorrecto");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Formato incorrecto", e.getHeader());
        }
    }

    @Test
    public void testValidarArchivoPDF_Nulo() {
        try {
            ValidadorDocumento.validarArchivoPDF(null);
            fail("Debió lanzar excepción por archivo nulo");
        } catch (ExcepcionMostrableUsuario e) {
            assertEquals("Archivo no seleccionado", e.getHeader());
        }
    }
}
