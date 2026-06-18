package main.service;

import java.util.List;
import main.business.dto.TitularProyectoDTO;
import main.database.dao.TitularProyectoDAO;
import main.common.UserDisplayableException;

public class TitularService {

    public static List<TitularProyectoDTO> getAllTitulares() throws UserDisplayableException {
        TitularProyectoDAO dao = new TitularProyectoDAO();
        return dao.getAll();
    }
}
