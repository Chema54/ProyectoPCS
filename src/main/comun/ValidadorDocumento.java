package main.comun;

import java.io.File;

public class ValidadorDocumento {
    
    public static void validarArchivoPDF(File file) throws ExcepcionMostrableUsuario {
        if (file == null) {
            throw new ExcepcionMostrableUsuario("Archivo no seleccionado", "Debe seleccionar un archivo", "No se ha proporcionado ningún archivo para subir.");
        }
        
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            throw new ExcepcionMostrableUsuario("Formato incorrecto", "Documento inválido", "El archivo debe tener el formato PDF (terminación .pdf).");
        }
    }
}
