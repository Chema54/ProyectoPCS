package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ProjectDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProjectDAO extends CompleteDAOShape<ProjectDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ProjectDAO.class);
    
    

    private static final String CREATE_QUERY =
            "INSERT INTO Proyecto (nombre, id_titular, estado, cupo_total, espacios_disponibles) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT p.*, o.razon_social as organizationName, t.nombre as titularName, t.numero_personal as titularNumeroPersonal FROM Proyecto p INNER JOIN TitularProyecto t ON p.id_titular = t.id_titular INNER JOIN OrganizacionVinculada o ON t.id_organizacion = o.id_organizacion";

    private static final String GET_QUERY =
            "SELECT p.*, o.razon_social as organizationName, t.nombre as titularName, t.numero_personal as titularNumeroPersonal FROM Proyecto p INNER JOIN TitularProyecto t ON p.id_titular = t.id_titular INNER JOIN OrganizacionVinculada o ON t.id_organizacion = o.id_organizacion WHERE p.id_proyecto = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Proyecto SET nombre = ?, id_titular = ?, estado = ?, cupo_total = ?, espacios_disponibles = ? WHERE id_proyecto = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Proyecto WHERE id_proyecto = ?";

    private static final String DECREMENT_SPACES_QUERY =
            "UPDATE Proyecto SET espacios_disponibles = espacios_disponibles - 1 WHERE id_proyecto = ?";
            
    private static final String CHANGE_STATUS_QUERY =
            "UPDATE Proyecto SET estado = ? WHERE id_proyecto = ?";

    @Override
    public void createOne(ProjectDTO projectDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, projectDTO.getName());
            
            if (projectDTO.getTitularId() != null) {
                statement.setInt(2, projectDTO.getTitularId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            
            statement.setString(3, projectDTO.getStatus() != null ? projectDTO.getStatus() : "Sin asignar");
            statement.setInt(4, projectDTO.getTotalCapacity());
            statement.setInt(5, projectDTO.getAvailableSpaces());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<ProjectDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ProjectDTO> projects = new ArrayList<>();

            while (resultSet.next()) {
                projects.add(mapResultSetToDTO(resultSet));
            }

            return projects;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public ProjectDTO getOne(Integer id) throws UserDisplayableException {
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(ProjectDTO projectDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, projectDTO.getName());
            
            if (projectDTO.getTitularId() != null) {
                statement.setInt(2, projectDTO.getTitularId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            
            statement.setString(3, projectDTO.getStatus());
            statement.setInt(4, projectDTO.getTotalCapacity());
            statement.setInt(5, projectDTO.getAvailableSpaces());
            statement.setInt(6, projectDTO.getProjectId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void decrementAvailableSpaces(int projectId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DECREMENT_SPACES_QUERY)
        ) {
            statement.setInt(1, projectId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }
    
    public void changeStatus(int projectId, String status) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_QUERY)
        ) {
            statement.setString(1, status);
            statement.setInt(2, projectId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }
    
    private ProjectDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        int titularId = resultSet.getInt("id_titular");
        Integer titularIdOrNull = resultSet.wasNull() ? null : titularId;
        
        return new ProjectDTO.ProjectBuilder()
            .setProjectId(resultSet.getInt("id_proyecto"))
            .setName(resultSet.getString("nombre"))
            .setTitularId(titularIdOrNull)
            .setStatus(resultSet.getString("estado"))
            .setTotalCapacity(resultSet.getInt("cupo_total"))
            .setAvailableSpaces(resultSet.getInt("espacios_disponibles"))
            .setOrganizationName(resultSet.getString("organizationName"))
            .setTitularDisplay(resultSet.getString("titularNumeroPersonal") + " - " + resultSet.getString("titularName"))
            .build();
    }
}
