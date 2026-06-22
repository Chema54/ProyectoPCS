package main.service;

import java.sql.Date;
import java.util.List;
import main.business.dto.PeriodoDTO;
import main.database.dao.PeriodoDAO;
import main.database.dao.ProyectoDAO;
import main.database.dao.AsignacionDAO;
import main.database.dao.DocumentoAceptacionDAO;
import main.database.dao.ReporteDAO;
import main.database.dao.AutoevaluacionDAO;
import main.database.dao.EvaluacionOVDAO;
import main.common.UserDisplayableException;

public class PeriodoService {

    public static List<PeriodoDTO> getAllPeriods() throws UserDisplayableException {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static void registerNewPeriod(PeriodoDTO period) throws UserDisplayableException {
        // Validaciones estrictas de negocio
        Date start = period.getStartDate();
        Date end = period.getEndDate();
        
        if (start != null && end != null && end.before(start)) {
             throw new UserDisplayableException(
                "Restricción de Periodo",
                "Fechas inválidas",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // Guardar Periodo en la BD
        PeriodoDAO dao = new PeriodoDAO();
        dao.createOne(period);
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
