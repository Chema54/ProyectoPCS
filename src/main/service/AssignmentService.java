package main.service;

import main.business.dto.AssignmentDTO;
import main.database.dao.AssignmentDAO;
import main.database.dao.ProjectDAO;
import main.database.dao.InternDAO;
import main.common.UserDisplayableException;

public class AssignmentService {

    public static void registerNewAssignment(AssignmentDTO assignment) throws UserDisplayableException {
        
        AssignmentDAO asignacionDAO = new AssignmentDAO();
        
        // 1. Insertar la asignación (Sin validaciones complejas, la GUI controla esto por tablas vacías)
        asignacionDAO.createOne(assignment);

        // 2. Disminuir espacios disponibles en el proyecto
        ProjectDAO proyectoDAO = new ProjectDAO();
        proyectoDAO.decrementAvailableSpaces(assignment.getProjectId());
        
        // 3. Cambiar estados a "Asignado" como dictan las postcondiciones del CU03
        proyectoDAO.changeStatus(assignment.getProjectId(), "Asignado");
        
        InternDAO practicanteDAO = new InternDAO();
        practicanteDAO.changeStatus(assignment.getInternId(), "Asignado");
    }
}
