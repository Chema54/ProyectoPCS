package main.service;

import java.util.List;
import main.business.dto.EducationalExperienceDTO;
import main.database.dao.EducationalExperienceDAO;
import main.common.UserDisplayableException;

public class EducationalExperienceService {

    public static List<EducationalExperienceDTO> getAllExperiencias() throws UserDisplayableException {
        EducationalExperienceDAO dao = new EducationalExperienceDAO();
        return dao.getAll();
    }
}
