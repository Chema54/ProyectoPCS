package main.service;

import java.util.List;
import main.business.dto.LinkedOrganizationDTO;
import main.database.dao.LinkedOrganizationDAO;
import main.common.UserDisplayableException;

public class LinkedOrganizationService {

    public static List<LinkedOrganizationDTO> getAllOrganizaciones() throws UserDisplayableException {
        LinkedOrganizationDAO dao = new LinkedOrganizationDAO();
        return dao.getAll();
    }
    
    public static void registrarNuevaOrganizacion(LinkedOrganizationDTO organizacion) throws UserDisplayableException {
        LinkedOrganizationDAO dao = new LinkedOrganizationDAO();
        
        if (dao.isNameRegistered(organizacion.getBusinessName())) {
            throw new UserDisplayableException(
                "Organización Duplicada",
                "El nombre de la organización ya existe en el sistema",
                "Esta Organización Vinculada ya se encuentra registrada en el sistema"
            );
        }
        
        dao.createOne(organizacion);
    }

    public static void actualizarOrganizacion(LinkedOrganizationDTO organizacion) throws UserDisplayableException {
        LinkedOrganizationDAO dao = new LinkedOrganizationDAO();
        dao.updateOne(organizacion);
    }
}
