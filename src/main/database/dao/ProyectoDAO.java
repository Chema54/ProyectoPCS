package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ProyectoDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProyectoDAO extends CompleteDAOShape<ProyectoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ProyectoDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Proyecto (nombre, id_titular, estado, cupo_total, espacios_disponibles) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Proyecto";

    private static final String GET_QUERY =
            "SELECT * FROM Proyecto WHERE id_proyecto = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Proyecto SET nombre = ?, id_titular = ?, estado = ?, cupo_total = ?, espacios_disponibles = ? WHERE id_proyecto = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Proyecto WHERE id_proyecto = ?";

    @Override
    public void createOne(ProyectoDTO projectDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, projectDTO.getName());
            statement.setInt(2, projectDTO.getTitularId());
            statement.setString(3, projectDTO.getStatus() != null ? projectDTO.getStatus() : "Sin asignar");
            statement.setInt(4, projectDTO.getTotalCapacity());
            statement.setInt(5, projectDTO.getAvailableSpaces());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del proyecto, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public List<ProyectoDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ProyectoDTO> projects = new ArrayList<>();

            while (resultSet.next()) {
                projects.add(new ProyectoDTO.ProyectoBuilder()
                    .setProjectId(resultSet.getInt("id_proyecto"))
                    .setName(resultSet.getString("nombre"))
                    .setTitularId(resultSet.getInt("id_titular"))
                    .setStatus(resultSet.getString("estado"))
                    .setTotalCapacity(resultSet.getInt("cupo_total"))
                    .setAvailableSpaces(resultSet.getInt("espacios_disponibles"))
                    .build()
                );
            }

            return projects;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de proyectos, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public ProyectoDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ProyectoDTO.ProyectoBuilder()
                        .setProjectId(resultSet.getInt("id_proyecto"))
                        .setName(resultSet.getString("nombre"))
                        .setTitularId(resultSet.getInt("id_titular"))
                        .setStatus(resultSet.getString("estado"))
                        .setTotalCapacity(resultSet.getInt("cupo_total"))
                        .setAvailableSpaces(resultSet.getInt("espacios_disponibles"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda del proyecto, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(ProyectoDTO projectDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, projectDTO.getName());
            statement.setInt(2, projectDTO.getTitularId());
            statement.setString(3, projectDTO.getStatus());
            statement.setInt(4, projectDTO.getTotalCapacity());
            statement.setInt(5, projectDTO.getAvailableSpaces());
            statement.setInt(6, projectDTO.getProjectId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del proyecto, debido a un error de conexión con la Base de datos");
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
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la eliminación del proyecto, debido a un error de conexión con la Base de datos");
        }
    }
}
