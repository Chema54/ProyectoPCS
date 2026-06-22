package main.service;

import main.business.dto.AsignacionDTO;
import main.database.dao.AsignacionDAO;
import main.database.dao.ProyectoDAO;
import main.database.dao.PracticanteDAO;
import main.common.UserDisplayableException;

public class AsignacionService {

    public static void registerNewAssignment(AsignacionDTO assignment) throws UserDisplayableException {
        
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        
        // 1. Insertar la asignación (Sin validaciones complejas, la GUI controla esto por tablas vacías)
        asignacionDAO.createOne(assignment);

        // 2. Disminuir espacios disponibles en el proyecto
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.decrementAvailableSpaces(assignment.getProjectId());
        
        // 3. Cambiar estados a "Asignado" como dictan las postcondiciones del CU03
        proyectoDAO.changeStatus(assignment.getProjectId(), "Asignado");
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        practicanteDAO.changeStatus(assignment.getInternId(), "Asignado");
    }
}
