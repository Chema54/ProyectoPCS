package main.service;

import main.business.dto.AsignacionDTO;
import main.database.dao.AsignacionDAO;
import main.database.dao.AutoevaluacionDAO;
import main.database.dao.DocumentoAceptacionDAO;
import main.database.dao.EvaluacionOVDAO;
import main.database.dao.ReporteMensualDAO;
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

        // Coordina a los DAO
        DocumentoAceptacionDAO.crearCascaron(idGenerado);
        ReporteMensualDAO.crearCascaron(idGenerado);
        AutoevaluacionDAO.crearCascaron(idGenerado);
        EvaluacionOVDAO.crearCascaron(idGenerado);
    }
}
