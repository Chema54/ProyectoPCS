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
            "INSERT INTO Evaluacion_OV (id_asignacion, archivo, estado, fecha_entrega, fecha_limite, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Evaluacion_OV";

    private static final String GET_QUERY =
            "SELECT * FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Evaluacion_OV SET id_asignacion = ?, archivo = ?, estado = ?, fecha_entrega = ?, fecha_limite = ?, calificacion = ?, comentarios = ? WHERE id_evaluacion_ov = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";
            
    private static final String CREATE_SHELL_QUERY =
            "INSERT INTO Evaluacion_OV (id_asignacion, estado) VALUES (?, 'Inhabilitado')";

    @Override
    public void createOne(EvaluacionOVDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getFile());
            statement.setString(3, evaluationDTO.getStatus() != null ? evaluationDTO.getStatus() : "Pendiente");
            statement.setDate(4, evaluationDTO.getDeliveryDate());
            statement.setDate(5, evaluationDTO.getDeadline());
            statement.setBigDecimal(6, evaluationDTO.getScore());
            statement.setString(7, evaluationDTO.getComments());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro de la evaluacion de organizacion vinculada, debido a un error de conexión con la Base de datos");
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
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la generación del cascarón de evaluacion OV, debido a un error de conexión con la Base de datos");
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
                evaluations.add(new EvaluacionOVDTO.EvaluacionOVBuilder()
                    .setLinkedOrganizationEvaluationId(resultSet.getInt("id_evaluacion_ov"))
                    .setAssignmentId(resultSet.getInt("id_asignacion"))
                    .setFile(resultSet.getString("archivo"))
                    .setStatus(resultSet.getString("estado"))
                    .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                    .setDeadline(resultSet.getDate("fecha_limite"))
                    .setScore(resultSet.getBigDecimal("calificacion"))
                    .setComments(resultSet.getString("comentarios"))
                    .build()
                );
            }

            return evaluations;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de evaluaciones de organizacion vinculada, debido a un error de conexión con la Base de datos");
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
                    return new EvaluacionOVDTO.EvaluacionOVBuilder()
                        .setLinkedOrganizationEvaluationId(resultSet.getInt("id_evaluacion_ov"))
                        .setAssignmentId(resultSet.getInt("id_asignacion"))
                        .setFile(resultSet.getString("archivo"))
                        .setStatus(resultSet.getString("estado"))
                        .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                        .setDeadline(resultSet.getDate("fecha_limite"))
                        .setScore(resultSet.getBigDecimal("calificacion"))
                        .setComments(resultSet.getString("comentarios"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda de la evaluacion de organizacion vinculada, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(EvaluacionOVDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getFile());
            statement.setString(3, evaluationDTO.getStatus());
            statement.setDate(4, evaluationDTO.getDeliveryDate());
            statement.setDate(5, evaluationDTO.getDeadline());
            statement.setBigDecimal(6, evaluationDTO.getScore());
            statement.setString(7, evaluationDTO.getComments());
            statement.setInt(8, evaluationDTO.getLinkedOrganizationEvaluationId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización de la evaluacion de organizacion vinculada, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación de la evaluacion de organizacion vinculada, debido a un error de conexión con la Base de datos");
        }
    }
}
