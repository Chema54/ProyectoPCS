package main.servicio;

import java.util.List;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.basedatos.dao.OrganizacionVinculadaDAO;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.Validador;

public class ServicioOrganizacionVinculada {

    public static List<OrganizacionVinculadaDTO> getAllOrganizaciones() throws ExcepcionMostrableUsuario {
        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        return dao.getAll();
    }

    public static void registrarNuevaOrganizacion(OrganizacionVinculadaDTO organizacion) throws ExcepcionMostrableUsuario {

        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();

        if (dao.isNameRegistered(organizacion.getNombreEmpresa())) {
            throw new ExcepcionMostrableUsuario(
                    "Organización Duplicada",
                    "El nombre de la organización ya existe en el sistema",
                    "Esta Organización Vinculada ya se encuentra registrada en el sistema"
            );
        }

        dao.createOne(organizacion);
    }

    public static void actualizarOrganizacion(OrganizacionVinculadaDTO organizacion) throws ExcepcionMostrableUsuario {

        OrganizacionVinculadaDAO dao = new OrganizacionVinculadaDAO();
        dao.updateOne(organizacion);
    }
}
