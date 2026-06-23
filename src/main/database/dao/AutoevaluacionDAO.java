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
            "INSERT INTO Autoevaluacion (id_asignacion, nombre_entregable, archivo, calificacion, comentarios, estado) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Autoevaluacion";

    private static final String GET_QUERY =
            "SELECT * FROM Autoevaluacion WHERE id_autoevaluacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Autoevaluacion SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, calificacion = ?, comentarios = ?, estado = ? WHERE id_autoevaluacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Autoevaluacion WHERE id_autoevaluacion = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Autoevaluacion (id_asignacion, nombre_entregable, estado) VALUES (?, 'Autoevaluación del estudiante', 'Inhabilitado')";

    @Override
    public void createOne(AutoevaluacionDTO selfAssessmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, selfAssessmentDTO.getAssignmentId());
            statement.setString(2, selfAssessmentDTO.getDeliverableName());
            statement.setBytes(3, selfAssessmentDTO.getFile());
            statement.setBigDecimal(4, selfAssessmentDTO.getScore());
            statement.setString(5, selfAssessmentDTO.getComments());
            statement.setString(6, selfAssessmentDTO.getStatus() != null ? selfAssessmentDTO.getStatus() : "Pendiente");

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void createDeliverables(List<Integer> assignmentIds) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)
        ) {
            for (Integer assignmentId : assignmentIds) {
                statement.setInt(1, assignmentId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
                selfAssessments.add(mapResultSetToDTO(resultSet));
            }

            return selfAssessments;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
                    return mapResultSetToDTO(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(AutoevaluacionDTO selfAssessmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, selfAssessmentDTO.getAssignmentId());
            statement.setString(2, selfAssessmentDTO.getDeliverableName());
            statement.setBytes(3, selfAssessmentDTO.getFile());
            statement.setBigDecimal(4, selfAssessmentDTO.getScore());
            statement.setString(5, selfAssessmentDTO.getComments());
            statement.setString(6, selfAssessmentDTO.getStatus());
            statement.setInt(7, selfAssessmentDTO.getSelfAssessmentId());
            
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
    
    private AutoevaluacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new AutoevaluacionDTO.AutoevaluacionBuilder()
            .setSelfAssessmentId(resultSet.getInt("id_autoevaluacion"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getBytes("archivo"))
            .setScore(resultSet.getBigDecimal("calificacion"))
            .setComments(resultSet.getString("comentarios"))
            .setStatus(resultSet.getString("estado"))
            .build();
    }
}
