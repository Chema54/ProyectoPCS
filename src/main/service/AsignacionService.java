package main.service;

import main.business.dto.AsignacionDTO;
import main.database.dao.AsignacionDAO;
import main.database.dao.AutoevaluacionDAO;
import main.database.dao.DocumentoAceptacionDAO;
import main.database.dao.EvaluacionOVDAO;
import main.database.dao.ReporteMensualDAO;
import main.database.dao.ProyectoDAO;
import main.common.UserDisplayableException;

public class AsignacionService {

    public static void registrarNuevaAsignacion(AsignacionDTO asignacion) throws UserDisplayableException {
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        
        // Validaciones
        if (asignacionDAO.hasActiveAssignment(asignacion.getInternId())) {
            throw new UserDisplayableException(
                "Restricción de asignación",
                "El alumno ya cuenta con un proyecto",
                "El Practicante ya tiene una asignación activa"
            );
        }

        int idGenerado = asignacionDAO.createOneAndReturnId(asignacion);

        if (idGenerado > 0) {
            // Generación en cascada de cascarones (Regla #5)
            DocumentoAceptacionDAO.crearCascaron(idGenerado);
            ReporteMensualDAO.crearCascaron(idGenerado);
            AutoevaluacionDAO.crearCascaron(idGenerado);
            EvaluacionOVDAO.crearCascaron(idGenerado);
            
            // Disminuir espacios en el proyecto
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            proyectoDAO.decrementAvailableSpaces(asignacion.getProjectId());
        } else {
            throw new UserDisplayableException(
                "Error de Registro",
                "No se pudo completar la asignación",
                "No se ha podido realizar La operación, debido a un error de conexión con la Base de datos"
            );
        }
    }
}
