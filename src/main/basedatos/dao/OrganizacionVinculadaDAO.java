package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.OrganizacionVinculadaDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrganizacionVinculadaDAO extends MoldeDAOCompleto<OrganizacionVinculadaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(OrganizacionVinculadaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO OrganizacionVinculada (razon_social, ubicacion, telefono, correo) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM OrganizacionVinculada";

    private static final String GET_QUERY =
            "SELECT * FROM OrganizacionVinculada WHERE id_organizacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE OrganizacionVinculada SET razon_social = ?, ubicacion = ?, telefono = ?, correo = ? WHERE id_organizacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM OrganizacionVinculada WHERE id_organizacion = ?";

    private static final String CHECK_NAME_QUERY =
            "SELECT COUNT(*) FROM OrganizacionVinculada WHERE razon_social = ?";

    @Override
    public void createOne(OrganizacionVinculadaDTO organizationDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, organizationDTO.getNombreEmpresa());
            statement.setString(2, organizationDTO.getDireccion());
            statement.setString(3, organizationDTO.getTelefono());
            statement.setString(4, organizationDTO.getCorreo());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible registrar la organización vinculada.");
        }
    }

    @Override
    public List<OrganizacionVinculadaDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<OrganizacionVinculadaDTO> organizations = new ArrayList<>();

            while (resultSet.next()) {
                organizations.add(mapResultSetToDTO(resultSet));
            }

            return organizations;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible cargar las organizaciones vinculadas.");
        }
    }

    @Override
    public OrganizacionVinculadaDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible obtener la organización vinculada.");
        }
    }

    @Override
    public void updateOne(OrganizacionVinculadaDTO organizationDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, organizationDTO.getNombreEmpresa());
            statement.setString(2, organizationDTO.getDireccion());
            statement.setString(3, organizationDTO.getTelefono());
            statement.setString(4, organizationDTO.getCorreo());
            statement.setInt(5, organizationDTO.getOrganizacionId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible actualizar la organización vinculada.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible eliminar la organización vinculada.");
        }
    }
    
    private OrganizacionVinculadaDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new OrganizacionVinculadaDTO.OrganizacionBuilder()
            .setOrganizacionId(resultSet.getInt("id_organizacion"))
            .setNombreEmpresa(resultSet.getString("razon_social"))
            .setDireccion(resultSet.getString("ubicacion"))
            .setTelefono(resultSet.getString("telefono"))
            .setCorreo(resultSet.getString("correo"))
            .build();
    }

    public boolean isNameRegistered(String nombreEmpresa) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_NAME_QUERY)
        ) {
            statement.setString(1, nombreEmpresa);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible verificar el nombre de la organización vinculada.");
        }
        return false;
    }
}
