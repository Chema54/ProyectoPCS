package main.service;

import java.util.List;
import main.business.dto.ProyectoDTO;
import main.database.dao.ProyectoDAO;
import main.database.dao.AsignacionDAO;
import main.database.dao.DocumentoAceptacionDAO;
import main.database.dao.ReporteDAO;
import main.database.dao.AutoevaluacionDAO;
import main.database.dao.EvaluacionOVDAO;
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

    public static void openPeriod(int periodId) throws UserDisplayableException {
        // 1. Activar los proyectos asociados a este periodo
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        List<Integer> projectIdsToActivate = asignacionDAO.getProjectIdsByPeriod(periodId);
        
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        for (Integer projectId : projectIdsToActivate) {
            proyectoDAO.changeStatus(projectId, "Activo");
        }
        
        // 2. Get all assignments associated with this period
        List<Integer> assignmentIds = asignacionDAO.getAssignmentIdsByPeriod(periodId);
        
        if (assignmentIds.isEmpty()) {
            return; // Nothing to create if no assignments exist
        }

        // 3. Execute inserts for all deliverables in 'Inhabilitado' state
        DocumentoAceptacionDAO documentoDAO = new DocumentoAceptacionDAO();
        documentoDAO.createDeliverables(assignmentIds);
        
        ReporteDAO reporteDAO = new ReporteDAO();
        reporteDAO.createDeliverables(assignmentIds);
        
        EvaluacionOVDAO evaluacionDAO = new EvaluacionOVDAO();
        evaluacionDAO.createDeliverables(assignmentIds);
        
        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        autoevaluacionDAO.createDeliverables(assignmentIds);
    }
}
