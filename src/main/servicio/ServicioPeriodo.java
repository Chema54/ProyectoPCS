package main.servicio;

import java.sql.Date;
import java.util.List;
import main.negocio.dto.PeriodoDTO;
import main.basedatos.dao.PeriodoDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.DocumentoAceptacionDAO;
import main.basedatos.dao.ReporteDAO;
import main.basedatos.dao.AutoevaluacionDAO;
import main.basedatos.dao.EvaluacionOVDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioPeriodo {

    public static List<PeriodoDTO> getAllPeriods() throws ExcepcionMostrableUsuario {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static void registerNewPeriod(PeriodoDTO period) throws ExcepcionMostrableUsuario {
        // Validaciones estrictas de negocio
        Date start = period.getFechaInicio();
        Date end = period.getFechaFin();
        
        if (start != null && end != null && end.before(start)) {
             throw new ExcepcionMostrableUsuario(
                "Restricción de Periodo",
                "Fechas inválidas",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // Guardar Periodo en la BD
        PeriodoDAO dao = new PeriodoDAO();
        dao.createOne(period);
    }

    public static void openPeriod(int periodoId) throws ExcepcionMostrableUsuario {
        // 1. Activar los proyectos asociados a este periodo
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        List<Integer> projectIdsToActivate = asignacionDAO.getProyectoIdsByPeriod(periodoId);
        
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        for (Integer proyectoId : projectIdsToActivate) {
            proyectoDAO.changeStatus(proyectoId, "Activo");
        }
        
        // 2. Get all assignments associated with this period
        List<Integer> assignmentIds = asignacionDAO.getAsignacionIdsByPeriod(periodoId);
        
        if (assignmentIds.isEmpty()) {
            throw new ExcepcionMostrableUsuario("Sin asignaciones", "No se puede abrir el periodo", "No hay proyectos asignados en este periodo.");
        }

        DocumentoAceptacionDAO documentoDAO = new DocumentoAceptacionDAO();
        if (!documentoDAO.getAllByAssignmentId(assignmentIds.get(0)).isEmpty()) {
            throw new ExcepcionMostrableUsuario("Operación Inválida", "El periodo ya fue abierto", "Ya se han generado los documentos para este periodo.");
        }

        // 3. Execute inserts for all deliverables in 'Inhabilitado' state
        documentoDAO.createEntregables(assignmentIds);
        
        ReporteDAO reporteDAO = new ReporteDAO();
        reporteDAO.createEntregables(assignmentIds);
        
        EvaluacionOVDAO evaluacionDAO = new EvaluacionOVDAO();
        evaluacionDAO.createEntregables(assignmentIds);
        
        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        autoevaluacionDAO.createEntregables(assignmentIds);
    }
}
