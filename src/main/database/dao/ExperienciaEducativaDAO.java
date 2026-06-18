package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ExperienciaEducativaDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExperienciaEducativaDAO extends CompleteDAOShape<ExperienciaEducativaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ExperienciaEducativaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO ExperienciaEducativa (nombre, id_periodo, nrc) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM ExperienciaEducativa";

    private static final String GET_QUERY =
            "SELECT * FROM ExperienciaEducativa WHERE id_experiencia = ?";

    private static final String UPDATE_QUERY =
            "UPDATE ExperienciaEducativa SET nombre = ?, id_periodo = ?, nrc = ? WHERE id_experiencia = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM ExperienciaEducativa WHERE id_experiencia = ?";

    @Override
    public void createOne(ExperienciaEducativaDTO experienceDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, experienceDTO.getName());
            statement.setInt(2, experienceDTO.getPeriodId());
            statement.setString(3, experienceDTO.getNrc());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro de la experiencia educativa, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public List<ExperienciaEducativaDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ExperienciaEducativaDTO> experiences = new ArrayList<>();

            while (resultSet.next()) {
                experiences.add(new ExperienciaEducativaDTO.ExperienciaEducativaBuilder()
                    .setEducationalExperienceId(resultSet.getInt("id_experiencia"))
                    .setName(resultSet.getString("nombre"))
                    .setPeriodId(resultSet.getInt("id_periodo"))
                    .setNrc(resultSet.getString("nrc"))
                    .build()
                );
            }

            return experiences;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de experiencias educativas, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public ExperienciaEducativaDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ExperienciaEducativaDTO.ExperienciaEducativaBuilder()
                        .setEducationalExperienceId(resultSet.getInt("id_experiencia"))
                        .setName(resultSet.getString("nombre"))
                        .setPeriodId(resultSet.getInt("id_periodo"))
                        .setNrc(resultSet.getString("nrc"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda de la experiencia educativa, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(ExperienciaEducativaDTO experienceDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, experienceDTO.getName());
            statement.setInt(2, experienceDTO.getPeriodId());
            statement.setString(3, experienceDTO.getNrc());
            statement.setInt(4, experienceDTO.getEducationalExperienceId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización de la experiencia educativa, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación de la experiencia educativa, debido a un error de conexión con la Base de datos");
        }
    }
}
