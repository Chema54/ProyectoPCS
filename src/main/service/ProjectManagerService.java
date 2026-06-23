package main.service;

import java.util.List;
import main.business.dto.ProjectManagerDTO;
import main.database.dao.ProjectManagerDAO;
import main.common.UserDisplayableException;

public class ProjectManagerService {

    public static List<ProjectManagerDTO> getAllTitulares() throws UserDisplayableException {
        ProjectManagerDAO dao = new ProjectManagerDAO();
        return dao.getAll();
    }
}
