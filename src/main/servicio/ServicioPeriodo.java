package main.servicio;

import java.util.List;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.AutoevaluacionDAO;
import main.basedatos.dao.DocumentoAceptacionDAO;
import main.basedatos.dao.EvaluacionOVDAO;
import main.basedatos.dao.PeriodoDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.ReporteDAO;
import main.comun.ExcepcionMostrableUsuario;
import main.negocio.dto.PeriodoDTO;

public class ServicioPeriodo {

    public static List<PeriodoDTO> getAllPeriods()
            throws ExcepcionMostrableUsuario {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static int registerNewPeriod(PeriodoDTO period)
            throws ExcepcionMostrableUsuario {

        if (period.getFechaInicio() == null || period.getFechaFin() == null) {
            throw new ExcepcionMostrableUsuario(
                    "Campos Obligatorios",
                    "Fechas no seleccionadas",
                    "Por favor, seleccione tanto la fecha de inicio como la fecha de fin del periodo."
            );
        }

        if (period.getFechaInicio().after(period.getFechaFin())) {
            throw new ExcepcionMostrableUsuario(
                    "Fechas Inválidas",
                    "El orden de las fechas es incorrecto",
                    "La fecha de inicio no puede ser posterior a la fecha de fin."
            );
        }

        PeriodoDAO dao = new PeriodoDAO();
        return dao.createAndReturnId(period);
    }

    public static void openPeriod(int periodoId)
            throws ExcepcionMostrableUsuario {

        PeriodoDAO periodoDAO = new PeriodoDAO();
        PeriodoDTO periodo = periodoDAO.getOne(periodoId);

        if (periodo == null) {
            throw new ExcepcionMostrableUsuario(
                    "Periodo no encontrado",
                    "No se puede abrir el periodo",
                    "El periodo seleccionado no existe en el sistema."
            );
        }

        if ("Activo".equalsIgnoreCase(periodo.getEstado())) {
            throw new ExcepcionMostrableUsuario(
                    "Periodo activo",
                    "El periodo ya se encuentra abierto",
                    "Seleccione un periodo que aún no haya sido abierto."
            );
        }

        AsignacionDAO asignacionDAO = new AsignacionDAO();

        List<Integer> assignmentIds =
                asignacionDAO.getAsignacionIdsByPeriod(periodoId);

        if (assignmentIds.isEmpty()) {
            throw new ExcepcionMostrableUsuario(
                    "Sin asignaciones",
                    "No se puede abrir el periodo",
                    "No hay proyectos asignados en este periodo."
            );
        }

        DocumentoAceptacionDAO documentoDAO =
                new DocumentoAceptacionDAO();

        if (!documentoDAO.getAllByAssignmentId(assignmentIds.get(0)).isEmpty()) {
            throw new ExcepcionMostrableUsuario(
                    "Operación Inválida",
                    "El periodo ya fue abierto",
                    "Ya se han generado los documentos para este periodo."
            );
        }

        List<Integer> projectIdsToActivate =
                asignacionDAO.getProyectoIdsByPeriod(periodoId);

        ProyectoDAO proyectoDAO = new ProyectoDAO();

        for (Integer proyectoId : projectIdsToActivate) {
            proyectoDAO.changeStatus(proyectoId, "Activo");
        }

        documentoDAO.createEntregables(assignmentIds);

        ReporteDAO reporteDAO = new ReporteDAO();
        reporteDAO.createEntregables(assignmentIds);

        EvaluacionOVDAO evaluacionDAO = new EvaluacionOVDAO();
        evaluacionDAO.createEntregables(assignmentIds);

        AutoevaluacionDAO autoevaluacionDAO = new AutoevaluacionDAO();
        autoevaluacionDAO.createEntregables(assignmentIds);

        periodoDAO.changeStatus(periodoId, "Activo");
    }
}