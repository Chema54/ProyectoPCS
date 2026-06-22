package main.service;

import java.util.List;
import main.business.dto.ProyectoDTO;
import main.database.dao.ProyectoDAO;
import main.common.UserDisplayableException;

public class ProyectoService {

    public static List<ProyectoDTO> getAllProyectos() throws UserDisplayableException {
        ProyectoDAO dao = new ProyectoDAO();
        return dao.getAll();
    }

    public static void registrarNuevoProyecto(ProyectoDTO proyecto) throws UserDisplayableException {
        
        // 1. Validaciones de negocio
        if (proyecto.getTotalCapacity() <= 0) {
             throw new UserDisplayableException(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // 2. Guardar Proyecto en la BD
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.createOne(proyecto);
    }
}
