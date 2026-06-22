package main.service;

import main.business.dto.AsignacionDTO;
import main.database.dao.AsignacionDAO;
import main.database.dao.ProyectoDAO;
import main.common.UserDisplayableException;

public class AsignacionService {

    public static void registerNewAssignment(AsignacionDTO assignment) throws UserDisplayableException {
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        
        // Validaciones
        if (asignacionDAO.hasActiveAssignment(assignment.getInternId())) {
            throw new UserDisplayableException(
                "Restricción de asignación",
                "El alumno ya cuenta con un proyecto",
                "El Practicante ya tiene una asignación activa"
            );
        }

        int idGenerado = asignacionDAO.createOneAndReturnId(assignment);

        if (idGenerado > 0) {
            // Disminuir espacios en el proyecto y cambiar estado a 'Activo'
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            proyectoDAO.decrementAvailableSpaces(assignment.getProjectId());
            proyectoDAO.changeStatus(assignment.getProjectId(), "Activo");
            
            // Nota: Ya NO se generan los entregables vacíos aquí. 
            // La generación ocurre masivamente durante "Abrir Periodo" (CU13).
        } else {
            throw new UserDisplayableException(
                "Error de Registro",
                "No se pudo completar la asignación",
                "No se ha podido realizar La operación, debido a un error de conexión con la Base de datos"
            );
        }
    }
}
