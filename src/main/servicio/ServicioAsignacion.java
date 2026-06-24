package main.servicio;

import main.negocio.dto.AsignacionDTO;
import main.basedatos.dao.AsignacionDAO;
import main.basedatos.dao.ProyectoDAO;
import main.basedatos.dao.PracticanteDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioAsignacion {

    public static void registerNewAssignment(AsignacionDTO assignment) throws ExcepcionMostrableUsuario {
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        
        // 1. Insertar la asignación (Sin validaciones complejas, la GUI controla esto por tablas vacías)
        asignacionDAO.createOne(assignment);

        // 2. Disminuir espacios disponibles en el proyecto
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.decrementAvailableSpaces(assignment.getProyectoId());
        
        // 3. Cambiar estados a "Asignado" como dictan las postcondiciones del CU03
        proyectoDAO.changeStatus(assignment.getProyectoId(), "Asignado");
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        practicanteDAO.changeStatus(assignment.getPracticanteId(), "Asignado");
    }
}
