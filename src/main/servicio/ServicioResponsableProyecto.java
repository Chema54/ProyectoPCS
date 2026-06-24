package main.servicio;

import java.util.List;
import main.negocio.dto.ResponsableProyectoDTO;
import main.basedatos.dao.ResponsableProyectoDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioResponsableProyecto {

    public static List<ResponsableProyectoDTO> getAllTitulares() throws ExcepcionMostrableUsuario {
        ResponsableProyectoDAO dao = new ResponsableProyectoDAO();
        return dao.getAll();
    }
}
