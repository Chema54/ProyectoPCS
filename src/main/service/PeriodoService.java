package main.service;

import java.sql.Date;
import java.util.List;
import main.business.dto.PeriodoDTO;
import main.database.dao.PeriodoDAO;
import main.database.dao.PeriodoProyectoDAO;
import main.database.dao.AsignacionDAO;
import main.database.dao.DocumentoAceptacionDAO;
import main.database.dao.ReporteMensualDAO;
import main.database.dao.AutoevaluacionDAO;
import main.database.dao.EvaluacionOVDAO;
import main.common.UserDisplayableException;

public class PeriodoService {

    public static List<PeriodoDTO> getAllPeriods() throws UserDisplayableException {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static void registerNewPeriod(PeriodoDTO period, List<Integer> projectIds) throws UserDisplayableException {
        // Validaciones estrictas de negocio
        
        Date start = period.getStartDate();
        Date end = period.getEndDate();
        
        if (start != null && end != null && end.before(start)) {
             throw new UserDisplayableException(
                "Restricción de Periodo",
                "Fechas inválidas",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado" // El usuario pidió mantener los literales
            );
        }
        
        // Guardar Periodo en la BD y vincular proyectos
        PeriodoDAO dao = new PeriodoDAO();
        Integer newPeriodId = dao.createAndReturnId(period);
        
        PeriodoProyectoDAO ppDao = new PeriodoProyectoDAO();
        ppDao.linkProjectsToPeriod(newPeriodId, projectIds);
    }
    
    public static void openPeriod(int periodId) throws UserDisplayableException {
        // 1. Get all assignments associated with this period
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        List<Integer> assignmentIds = asignacionDAO.getAssignmentIdsByPeriod(periodId);
        
        if (assignmentIds.isEmpty()) {
            return; // Nothing to create if no assignments exist
        }

        // 2. Execute inserts for all deliverables in 'Inhabilitado' state
        DocumentoAceptacionDAO documentoDAO = new DocumentoAceptacionDAO();
        documentoDAO.createDeliverables(assignmentIds);
        
        ReporteMensualDAO reporteDAO = new ReporteMensualDAO();
        reporteDAO.createDeliverables(assignmentIds);
        
        EvaluacionOVDAO evaluacionDAO = new EvaluacionOVDAO();
        evaluacionDAO.createDeliverables(assignmentIds);
        
        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        autoevaluacionDAO.createDeliverables(assignmentIds);
        
        // At this point, all deliverables for all assignments in the period 
        // have been created in the database with their respective specific titles.
    }
}
