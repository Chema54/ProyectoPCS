package main.service;

import java.util.List;
import main.business.dto.ExperienciaEducativaDTO;
import main.database.dao.ExperienciaEducativaDAO;
import main.common.UserDisplayableException;

public class ExperienciaEducativaService {

    public static List<ExperienciaEducativaDTO> getAllExperiencias() throws UserDisplayableException {
        ExperienciaEducativaDAO dao = new ExperienciaEducativaDAO();
        return dao.getAll();
    }
}
