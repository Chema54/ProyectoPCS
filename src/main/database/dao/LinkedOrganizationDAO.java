package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.LinkedOrganizationDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LinkedOrganizationDAO extends CompleteDAOShape<LinkedOrganizationDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(LinkedOrganizationDAO.class);

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
    public void createOne(LinkedOrganizationDTO organizationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, organizationDTO.getBusinessName());
            statement.setString(2, organizationDTO.getLocation());
            statement.setString(3, organizationDTO.getPhoneNumber());
            statement.setString(4, organizationDTO.getEmail());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible registrar la organización vinculada.");
        }
    }

    @Override
    public List<LinkedOrganizationDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<LinkedOrganizationDTO> organizations = new ArrayList<>();

            while (resultSet.next()) {
                organizations.add(mapResultSetToDTO(resultSet));
            }

            return organizations;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las organizaciones vinculadas.");
        }
    }

    @Override
    public LinkedOrganizationDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener la organización vinculada.");
        }
    }

    @Override
    public void updateOne(LinkedOrganizationDTO organizationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, organizationDTO.getBusinessName());
            statement.setString(2, organizationDTO.getLocation());
            statement.setString(3, organizationDTO.getPhoneNumber());
            statement.setString(4, organizationDTO.getEmail());
            statement.setInt(5, organizationDTO.getOrganizationId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar la organización vinculada.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la organización vinculada.");
        }
    }
    
    private LinkedOrganizationDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new LinkedOrganizationDTO.LinkedOrganizationBuilder()
            .setOrganizationId(resultSet.getInt("id_organizacion"))
            .setBusinessName(resultSet.getString("razon_social"))
            .setLocation(resultSet.getString("ubicacion"))
            .setPhoneNumber(resultSet.getString("telefono"))
            .setEmail(resultSet.getString("correo"))
            .build();
    }

    public boolean isNameRegistered(String businessName) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_NAME_QUERY)
        ) {
            statement.setString(1, businessName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible verificar el nombre de la organización vinculada.");
        }
        return false;
    }
}
