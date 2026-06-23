package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.LinkedOrganizationEvaluationDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LinkedOrganizationEvaluationDAO extends CompleteDAOShape<LinkedOrganizationEvaluationDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(LinkedOrganizationEvaluationDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, archivo, estado, fecha_limite, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Evaluacion_OV";

    private static final String GET_QUERY =
            "SELECT * FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Evaluacion_OV SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_limite = ?, calificacion = ?, comentarios = ? WHERE id_evaluacion_ov = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(LinkedOrganizationEvaluationDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getDeliverableName());
            statement.setBytes(3, evaluationDTO.getFile());
            statement.setString(4, evaluationDTO.getStatus() != null ? evaluationDTO.getStatus() : "Pendiente");
            statement.setDate(5, evaluationDTO.getDeadline());
            statement.setBigDecimal(6, evaluationDTO.getScore());
            statement.setString(7, evaluationDTO.getComments());

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
    public List<LinkedOrganizationEvaluationDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<LinkedOrganizationEvaluationDTO> evaluations = new ArrayList<>();

            while (resultSet.next()) {
                evaluations.add(mapResultSetToDTO(resultSet));
            }

            return evaluations;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public LinkedOrganizationEvaluationDTO getOne(Integer id) throws UserDisplayableException {
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
    public void updateOne(LinkedOrganizationEvaluationDTO evaluationDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, evaluationDTO.getAssignmentId());
            statement.setString(2, evaluationDTO.getDeliverableName());
            statement.setBytes(3, evaluationDTO.getFile());
            statement.setString(4, evaluationDTO.getStatus());
            statement.setDate(5, evaluationDTO.getDeadline());
            statement.setBigDecimal(6, evaluationDTO.getScore());
            statement.setString(7, evaluationDTO.getComments());
            statement.setInt(8, evaluationDTO.getLinkedOrganizationEvaluationId());
            
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
    
    public List<LinkedOrganizationEvaluationDTO> getAllByAssignmentId(int assignmentId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Evaluacion_OV WHERE id_asignacion = ?")
        ) {
            statement.setInt(1, assignmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<LinkedOrganizationEvaluationDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error consultando las evaluaciones.");
        }
    }

        public List<LinkedOrganizationEvaluationDTO> getUniqueDeliverablesByExperiencia(int idExperiencia) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT r.nombre_entregable, r.estado FROM Evaluacion_OV r INNER JOIN Asignacion a ON r.id_asignacion = a.id_asignacion WHERE a.id_experiencia = ?")
        ) {
            statement.setInt(1, idExperiencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<LinkedOrganizationEvaluationDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new LinkedOrganizationEvaluationDTO.LinkedOrganizationEvaluationBuilder()
                        .setDeliverableName(resultSet.getString("nombre_entregable"))
                        .setStatus(resultSet.getString("estado"))
                        .build());
                }
                return list;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error consultando las evaluaciones únicas.");
        }
    }

    public void enableDeliverablesMasive(String nombreDoc, int idExperiencia, java.sql.Date fechaLimite) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Evaluacion_OV SET estado = 'Habilitado', fecha_limite = ? WHERE nombre_entregable = ? AND id_asignacion IN (SELECT id_asignacion FROM Asignacion WHERE id_experiencia = ?)")
        ) {
            statement.setDate(1, fechaLimite);
            statement.setString(2, nombreDoc);
            statement.setInt(3, idExperiencia);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error habilitando evaluaciones.");
        }
    }

private LinkedOrganizationEvaluationDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new LinkedOrganizationEvaluationDTO.LinkedOrganizationEvaluationBuilder()
            .setLinkedOrganizationEvaluationId(resultSet.getInt("id_evaluacion_ov"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getBytes("archivo"))
            .setStatus(resultSet.getString("estado"))
            .setDeadline(resultSet.getDate("fecha_limite"))
            .setScore(resultSet.getBigDecimal("calificacion"))
            .setComments(resultSet.getString("comentarios"))
            .build();
    }
}
