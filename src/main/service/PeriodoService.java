package main.service;

import java.sql.Date;
import java.util.List;
import main.business.dto.PeriodoDTO;
import main.database.dao.PeriodoDAO;
import main.common.UserDisplayableException;

public class PeriodoService {

    public static List<PeriodoDTO> getAllPeriods() throws UserDisplayableException {
        PeriodoDAO dao = new PeriodoDAO();
        return dao.getAll();
    }

    public static void registerNewPeriod(PeriodoDTO period) throws UserDisplayableException {
        // Validaciones estrictas de negocio
        Date start = period.getStartDate();
        Date end = period.getEndDate();
        
        if (start != null && end != null && end.before(start)) {
             throw new UserDisplayableException(
                "Restricción de Periodo",
                "Fechas inválidas",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        // Guardar Periodo en la BD
        PeriodoDAO dao = new PeriodoDAO();
        dao.createOne(period);
    }
}
