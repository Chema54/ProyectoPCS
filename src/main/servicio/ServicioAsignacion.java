package main.servicio;

import main.negocio.dto.AsignacionDTO;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.PracticanteDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioAsignacion {

    public static void registerNewAssignment(AsignacionDTO assignment) throws ExcepcionMostrableUsuario {
        
        if (assignment.getPracticanteId() <= 0 || assignment.getProyectoId() <= 0) {
            throw new ExcepcionMostrableUsuario("Campos Incompletos", "Datos faltantes", "Debe seleccionar un practicante y un proyecto válidos.");
        }
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        
        // Validar si el proyecto tiene cupo
        main.negocio.dto.ProyectoDTO proyecto = proyectoDAO.getOne(assignment.getProyectoId());
        if (proyecto != null && proyecto.getEspaciosDisponibles() <= 0) {
            throw new ExcepcionMostrableUsuario("Cupo Insuficiente", "El proyecto seleccionado no tiene espacios", "No es posible asignar al practicante porque el proyecto seleccionado ya está lleno.");
        }
        
        // 1. Insertar la asignación
        asignacionDAO.createOne(assignment);

        // 2. Disminuir espacios disponibles en el proyecto
        proyectoDAO.decrementAvailableSpaces(assignment.getProyectoId());
        
        // 3. Cambiar estados a "Asignado" como dictan las postcondiciones del CU03
        proyectoDAO.changeStatus(assignment.getProyectoId(), "Asignado");
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        practicanteDAO.changeStatus(assignment.getPracticanteId(), "Asignado");
    }
}
