package main.service;

import java.util.List;
import main.business.dto.ProjectDTO;
import main.database.dao.ProjectDAO;
import main.business.dto.ProjectManagerDTO;
import main.database.dao.ProjectManagerDAO;
import main.common.UserDisplayableException;

public class ProjectService {

    public static List<ProjectDTO> getAllProyectos() throws UserDisplayableException {
        ProjectDAO dao = new ProjectDAO();
        return dao.getAll();
    }

    public static void registrarNuevoProyecto(ProjectDTO proyecto, ProjectManagerDTO titular) throws UserDisplayableException {
        
        // 1. Validaciones de negocio
        if (proyecto.getTotalCapacity() <= 0) {
             throw new UserDisplayableException(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // 2. Guardar Titular y obtener
        ProjectManagerDAO titularDAO = new ProjectManagerDAO();
        titularDAO.createOne(titular);
        ProjectManagerDTO titularGuardado = titularDAO.getByNumeroPersonal(titular.getNumeroPersonal());

        // 3. Recrear DTO del Proyecto con el ID del titular
        ProjectDTO proyectoFinal = new ProjectDTO.ProjectBuilder()
                .setName(proyecto.getName())
                .setTitularId(titularGuardado.getTitularId())
                .setTotalCapacity(proyecto.getTotalCapacity())
                .setAvailableSpaces(proyecto.getAvailableSpaces())
                .setStatus(proyecto.getStatus())
                .build();
        
        // 4. Guardar Proyecto en la BD
        ProjectDAO proyectoDAO = new ProjectDAO();
        proyectoDAO.createOne(proyectoFinal);
    }

    public static void actualizarProyecto(ProjectDTO proyecto, ProjectManagerDTO titular) throws UserDisplayableException {
        // 1. Validaciones de negocio
        if (proyecto.getTotalCapacity() <= 0) {
             throw new UserDisplayableException(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // 2. Actualizar Titular
        ProjectManagerDAO titularDAO = new ProjectManagerDAO();
        titularDAO.updateOne(titular);
        
        // 3. Recrear DTO del Proyecto con el ID del titular que se mantuvo
        ProjectDTO proyectoFinal = new ProjectDTO.ProjectBuilder()
                .setProjectId(proyecto.getProjectId())
                .setName(proyecto.getName())
                .setTitularId(titular.getTitularId())
                .setTotalCapacity(proyecto.getTotalCapacity())
                .setAvailableSpaces(proyecto.getAvailableSpaces())
                .setStatus(proyecto.getStatus())
                .build();
        
        // 4. Actualizar Proyecto en la BD
        ProjectDAO proyectoDAO = new ProjectDAO();
        proyectoDAO.updateOne(proyectoFinal);
    }
}
