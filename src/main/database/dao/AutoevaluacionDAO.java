package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.AutoevaluacionDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoevaluacionDAO extends CompleteDAOShape<AutoevaluacionDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(AutoevaluacionDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Autoevaluacion (id_asignacion, calificacion, comentarios) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Autoevaluacion";

    private static final String GET_QUERY =
            "SELECT * FROM Autoevaluacion WHERE id_autoevaluacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Autoevaluacion SET id_asignacion = ?, calificacion = ?, comentarios = ? WHERE id_autoevaluacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Autoevaluacion WHERE id_autoevaluacion = ?";
            
    private static final String CREATE_SHELL_QUERY =
            "INSERT INTO Autoevaluacion (id_asignacion, estado) VALUES (?, 'Inhabilitado')";

    @Override
    public void createOne(AutoevaluacionDTO selfAssessmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, selfAssessmentDTO.getAssignmentId());
            statement.setBigDecimal(2, selfAssessmentDTO.getScore());
            statement.setString(3, selfAssessmentDTO.getComments());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e);
        }
    }

    public static void crearCascaron(int assignmentId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_SHELL_QUERY)
        ) {
            statement.setInt(1, assignmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e);
        }
    }

    @Override
    public List<AutoevaluacionDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<AutoevaluacionDTO> selfAssessments = new ArrayList<>();

            while (resultSet.next()) {
                selfAssessments.add(new AutoevaluacionDTO.AutoevaluacionBuilder()
                    .setSelfAssessmentId(resultSet.getInt("id_autoevaluacion"))
                    .setAssignmentId(resultSet.getInt("id_asignacion"))
                    .setScore(resultSet.getBigDecimal("calificacion"))
                    .setComments(resultSet.getString("comentarios"))
                    .build()
                );
            }

            return selfAssessments;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de autoevaluaciones, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public AutoevaluacionDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new AutoevaluacionDTO.AutoevaluacionBuilder()
                        .setSelfAssessmentId(resultSet.getInt("id_autoevaluacion"))
                        .setAssignmentId(resultSet.getInt("id_asignacion"))
                        .setScore(resultSet.getBigDecimal("calificacion"))
                        .setComments(resultSet.getString("comentarios"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda de la autoevaluacion, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(AutoevaluacionDTO selfAssessmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, selfAssessmentDTO.getAssignmentId());
            statement.setBigDecimal(2, selfAssessmentDTO.getScore());
            statement.setString(3, selfAssessmentDTO.getComments());
            statement.setInt(4, selfAssessmentDTO.getSelfAssessmentId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización de la autoevaluacion, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación de la autoevaluacion, debido a un error de conexión con la Base de datos");
        }
    }
}
