package main.service;

import java.util.List;
import main.business.dto.ProyectoDTO;
import main.database.dao.ProyectoDAO;
import main.business.dto.TitularProyectoDTO;
import main.database.dao.TitularProyectoDAO;
import main.common.UserDisplayableException;

public class ProyectoService {

    public static List<ProyectoDTO> getAllProyectos() throws UserDisplayableException {
        ProyectoDAO dao = new ProyectoDAO();
        return dao.getAll();
    }

    public static void registrarNuevoProyecto(ProyectoDTO proyecto, TitularProyectoDTO titular) throws UserDisplayableException {
        
        // 1. Validaciones de negocio
        if (proyecto.getTotalCapacity() <= 0) {
             throw new UserDisplayableException(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // 2. Guardar Titular y obtener
        TitularProyectoDAO titularDAO = new TitularProyectoDAO();
        titularDAO.createOne(titular);
        TitularProyectoDTO titularGuardado = titularDAO.getByNumeroPersonal(titular.getNumeroPersonal());

        // 3. Recrear DTO del Proyecto con el ID del titular
        ProyectoDTO proyectoFinal = new ProyectoDTO.ProyectoBuilder()
                .setName(proyecto.getName())
                .setTitularId(titularGuardado.getTitularId())
                .setTotalCapacity(proyecto.getTotalCapacity())
                .setAvailableSpaces(proyecto.getAvailableSpaces())
                .setStatus(proyecto.getStatus())
                .build();
        
        // 4. Guardar Proyecto en la BD
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.createOne(proyectoFinal);
    }

    public static void actualizarProyecto(ProyectoDTO proyecto, TitularProyectoDTO titular) throws UserDisplayableException {
        // 1. Validaciones de negocio
        if (proyecto.getTotalCapacity() <= 0) {
             throw new UserDisplayableException(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // 2. Actualizar Titular
        TitularProyectoDAO titularDAO = new TitularProyectoDAO();
        titularDAO.updateOne(titular);
        
        // 3. Recrear DTO del Proyecto con el ID del titular que se mantuvo
        ProyectoDTO proyectoFinal = new ProyectoDTO.ProyectoBuilder()
                .setProjectId(proyecto.getProjectId())
                .setName(proyecto.getName())
                .setTitularId(titular.getTitularId())
                .setTotalCapacity(proyecto.getTotalCapacity())
                .setAvailableSpaces(proyecto.getAvailableSpaces())
                .setStatus(proyecto.getStatus())
                .build();
        
        // 4. Actualizar Proyecto en la BD
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateOne(proyectoFinal);
    }
}
