package main.service;

import java.util.List;
import main.business.dto.OrganizacionVinculadaDTO;
import main.database.dao.OrganizacionVinculadaDAO;
import main.common.UserDisplayableException;

public class OrganizacionService {

    public static List<OrganizacionVinculadaDTO> getAllOrganizaciones() throws UserDisplayableException {
        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        return dao.getAll();
    }
    
    public static void registrarNuevaOrganizacion(OrganizacionVinculadaDTO organizacion) throws UserDisplayableException {
        // Validaciones de negocio pueden ir aquí
        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        dao.createOne(organizacion);
    }
}
