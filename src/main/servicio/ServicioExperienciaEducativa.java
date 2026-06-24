package main.servicio;

import java.util.List;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.basedatos.dao.ExperienciaEducativaDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioExperienciaEducativa {

    public static List<ExperienciaEducativaDTO> getAllExperiencias() throws ExcepcionMostrableUsuario {
        ExperienciaEducativaDAO dao = new ExperienciaEducativaDAO();
        return dao.getAll();
    }
}
