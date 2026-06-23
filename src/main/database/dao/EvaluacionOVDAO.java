package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.EvaluacionOVDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EvaluacionOVDAO extends CompleteDAOShape<EvaluacionOVDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(EvaluacionOVDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, archivo, estado, fecha_entrega, fecha_limite, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Evaluacion_OV";

    private static final String GET_QUERY =
            "SELECT * FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Evaluacion_OV SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_entrega = ?, fecha_limite = ?, calificacion = ?, comentarios = ? WHERE id_evaluacion_ov = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(EvaluacionOVDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getDeliverableName());
            statement.setBytes(3, evaluationDTO.getFile());
            statement.setString(4, evaluationDTO.getStatus() != null ? evaluationDTO.getStatus() : "Pendiente");
            statement.setDate(5, evaluationDTO.getDeliveryDate());
            statement.setDate(6, evaluationDTO.getDeadline());
            statement.setBigDecimal(7, evaluationDTO.getScore());
            statement.setString(8, evaluationDTO.getComments());

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
                // 2 inserts required per assignment
                statement.setInt(1, assignmentId);
                statement.setString(2, "Primera evaluación de la OV");
                statement.executeUpdate();
                
                statement.setInt(1, assignmentId);
                statement.setString(2, "Segunda evaluación de la OV");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<EvaluacionOVDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<EvaluacionOVDTO> evaluations = new ArrayList<>();

            while (resultSet.next()) {
                evaluations.add(mapResultSetToDTO(resultSet));
            }

            return evaluations;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public EvaluacionOVDTO getOne(Integer id) throws UserDisplayableException {
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
    public void updateOne(EvaluacionOVDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getDeliverableName());
            statement.setBytes(3, evaluationDTO.getFile());
            statement.setString(4, evaluationDTO.getStatus());
            statement.setDate(5, evaluationDTO.getDeliveryDate());
            statement.setDate(6, evaluationDTO.getDeadline());
            statement.setBigDecimal(7, evaluationDTO.getScore());
            statement.setString(8, evaluationDTO.getComments());
            statement.setInt(9, evaluationDTO.getLinkedOrganizationEvaluationId());
            
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
    
    private EvaluacionOVDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new EvaluacionOVDTO.EvaluacionOVBuilder()
            .setLinkedOrganizationEvaluationId(resultSet.getInt("id_evaluacion_ov"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getBytes("archivo"))
            .setStatus(resultSet.getString("estado"))
            .setDeliveryDate(resultSet.getDate("fecha_entrega"))
            .setDeadline(resultSet.getDate("fecha_limite"))
            .setScore(resultSet.getBigDecimal("calificacion"))
            .setComments(resultSet.getString("comentarios"))
            .build();
    }
}
