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
        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        
        if (dao.isNameRegistered(organizacion.getBusinessName())) {
            throw new UserDisplayableException(
                "Organización Duplicada",
                "El nombre de la organización ya existe en el sistema",
                "Esta Organización Vinculada ya se encuentra registrada en el sistema"
            );
        }
        
        dao.createOne(organizacion);
    }

    public static void actualizarOrganizacion(OrganizacionVinculadaDTO organizacion) throws UserDisplayableException {
        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        dao.updateOne(organizacion);
    }
}
