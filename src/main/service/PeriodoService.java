package main.service;

import java.sql.Date;
import java.util.List;
import main.business.dto.PeriodoDTO;
import main.database.dao.PeriodoDAO;
import main.database.dao.PeriodoProyectoDAO;
import main.common.UserDisplayableException;

public class PeriodoService {

    public static List<PeriodoDTO> getAllPeriodos() throws UserDisplayableException {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static void registrarNuevoPeriodo(PeriodoDTO periodo, List<Integer> projectIds) throws UserDisplayableException {
        // Validaciones estrictas de negocio
        
        Date start = periodo.getStartDate();
        Date end = periodo.getEndDate();
        
        if (start != null && end != null && end.before(start)) {
             throw new UserDisplayableException(
                "Restricción de Periodo",
                "Fechas inválidas",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado" // El usuario pidió mantener los literales
            );
        }
        
        // Guardar Periodo en la BD y vincular proyectos
        PeriodoDAO dao = new PeriodoDAO();
        Integer newPeriodId = dao.createAndReturnId(periodo);
        
        PeriodoProyectoDAO ppDao = new PeriodoProyectoDAO();
        ppDao.linkProjectsToPeriod(newPeriodId, projectIds);
    }
}
