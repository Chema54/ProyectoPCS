package main.service;

import java.sql.Date;
import java.util.List;
import main.business.dto.PeriodDTO;
import main.database.dao.PeriodDAO;
import main.database.dao.ProjectDAO;
import main.database.dao.AssignmentDAO;
import main.database.dao.AcceptanceDocumentDAO;
import main.database.dao.ReportDAO;
import main.database.dao.SelfAssessmentDAO;
import main.database.dao.LinkedOrganizationEvaluationDAO;
import main.common.UserDisplayableException;

public class PeriodService {

    public static List<PeriodDTO> getAllPeriods() throws UserDisplayableException {
        PeriodDAO dao = new PeriodDAO();
        return dao.getAll();
    }

    public static void registerNewPeriod(PeriodDTO period) throws UserDisplayableException {
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
        PeriodDAO dao = new PeriodDAO();
        dao.createOne(period);
    }

    public static void openPeriod(int periodId) throws UserDisplayableException {
        // 1. Activar los proyectos asociados a este periodo
        AssignmentDAO asignacionDAO = new AssignmentDAO();
        List<Integer> projectIdsToActivate = asignacionDAO.getProjectIdsByPeriod(periodId);
        
        ProjectDAO proyectoDAO = new ProjectDAO();
        for (Integer projectId : projectIdsToActivate) {
            proyectoDAO.changeStatus(projectId, "Activo");
        }
        
        // 2. Get all assignments associated with this period
        List<Integer> assignmentIds = asignacionDAO.getAssignmentIdsByPeriod(periodId);
        
        if (assignmentIds.isEmpty()) {
            return; // Nothing to create if no assignments exist
        }

        // 3. Execute inserts for all deliverables in 'Inhabilitado' state
        AcceptanceDocumentDAO documentoDAO = new AcceptanceDocumentDAO();
        documentoDAO.createDeliverables(assignmentIds);
        
        ReportDAO reporteDAO = new ReportDAO();
        reporteDAO.createDeliverables(assignmentIds);
        
        LinkedOrganizationEvaluationDAO evaluacionDAO = new LinkedOrganizationEvaluationDAO();
        evaluacionDAO.createDeliverables(assignmentIds);
        
        SelfAssessmentDAO autoevaluacionDAO = new SelfAssessmentDAO();
        autoevaluacionDAO.createDeliverables(assignmentIds);
    }
}
