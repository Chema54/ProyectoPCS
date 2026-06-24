package main.servicio;

import java.util.List;
import java.util.stream.Collectors;
import main.basedatos.dao.DocumentoAceptacionDAO;
import main.negocio.dto.DocumentoAceptacionDTO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioDocumento {

    private final DocumentoAceptacionDAO documentoDAO = new DocumentoAceptacionDAO();

    public List<DocumentoAceptacionDTO> obtenerDocumentosEntregados() throws ExcepcionMostrableUsuario {
        List<DocumentoAceptacionDTO> todos = documentoDAO.getAll();
        return todos.stream()
                .filter(doc -> "Entregado".equalsIgnoreCase(doc.getEstado()))
                .collect(Collectors.toList());
    }

}
