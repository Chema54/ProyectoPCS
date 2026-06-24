package main.servicio;

import main.negocio.dto.AsignacionDTO;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.PracticanteDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioAsignacion {

    public static void registrarNuevaAsignacion(AsignacionDTO assignment) throws ExcepcionMostrableUsuario {
        
        if (assignment.getPracticanteId() <= 0 || assignment.getProyectoId() <= 0 || assignment.getExperienciaEducativaId() <= 0) {
            throw new ExcepcionMostrableUsuario("Campos Incompletos", "Datos faltantes", "Debe seleccionar un practicante, un proyecto y una experiencia educativa válidos.");
        }
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        
        if (asignacionDAO.hasActiveAssignment(assignment.getPracticanteId())) {
            throw new ExcepcionMostrableUsuario("Asignación Duplicada", "El practicante ya tiene una asignación", "El practicante seleccionado ya cuenta con un proyecto asignado en estado Activo.");
        }

        ProyectoDAO proyectoDAO = new ProyectoDAO();
        main.negocio.dto.ProyectoDTO proyecto = proyectoDAO.getOne(assignment.getProyectoId());
        if (proyecto != null && proyecto.getEspaciosDisponibles() <= 0) {
            throw new ExcepcionMostrableUsuario("Cupo Insuficiente", "El proyecto seleccionado no tiene espacios", "No es posible asignar al practicante porque el proyecto seleccionado ya está lleno.");
        }
        
        main.basedatos.dao.ExperienciaEducativaDAO eeDAO = new main.basedatos.dao.ExperienciaEducativaDAO();
        main.negocio.dto.ExperienciaEducativaDTO ee = eeDAO.getOne(assignment.getExperienciaEducativaId());
        if (ee != null && ee.getPeriodoId() != null) {
            main.basedatos.dao.PeriodoDAO periodoDAO = new main.basedatos.dao.PeriodoDAO();
            main.negocio.dto.PeriodoDTO periodo = periodoDAO.getOne(ee.getPeriodoId());
            if (periodo != null && "Activo".equals(periodo.getEstado())) {
                throw new ExcepcionMostrableUsuario("Periodo Inválido", "El periodo ya está en curso", "No se pueden realizar nuevas asignaciones en un periodo escolar que ya ha sido abierto.");
            }
        }
        
        asignacionDAO.createOne(assignment);

        // Resta local en Java para mantener la trazabilidad del cupo disponible del proyecto (aunque MySQL también podría validarlo).
        proyectoDAO.decrementAvailableSpaces(assignment.getProyectoId());
        
        // Sincronización de estados según Postcondiciones de CU03: Transición a "Asignado".
        proyectoDAO.changeStatus(assignment.getProyectoId(), "Asignado");
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        practicanteDAO.changeStatus(assignment.getPracticanteId(), "Asignado");
    }
}
