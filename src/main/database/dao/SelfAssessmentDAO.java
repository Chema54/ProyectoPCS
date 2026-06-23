package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.SelfAssessmentDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SelfAssessmentDAO extends CompleteDAOShape<SelfAssessmentDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(SelfAssessmentDAO.class);
    

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
    public void createOne(SelfAssessmentDTO selfAssessmentDTO) throws UserDisplayableException {
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
    public List<SelfAssessmentDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<SelfAssessmentDTO> selfAssessments = new ArrayList<>();

            while (resultSet.next()) {
                selfAssessments.add(mapResultSetToDTO(resultSet));
            }

            return selfAssessments;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public SelfAssessmentDTO getOne(Integer id) throws UserDisplayableException {
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
    public void updateOne(SelfAssessmentDTO selfAssessmentDTO) throws UserDisplayableException {
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
    
    public List<SelfAssessmentDTO> getAllByAssignmentId(int assignmentId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Autoevaluacion WHERE id_asignacion = ?")
        ) {
            statement.setInt(1, assignmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<SelfAssessmentDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error consultando las autoevaluaciones.");
        }
    }

        public List<SelfAssessmentDTO> getUniqueDeliverablesByExperiencia(int idExperiencia) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT r.nombre_entregable, r.estado FROM Autoevaluacion r INNER JOIN Asignacion a ON r.id_asignacion = a.id_asignacion WHERE a.id_experiencia = ?")
        ) {
            statement.setInt(1, idExperiencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<SelfAssessmentDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new SelfAssessmentDTO.SelfAssessmentBuilder()
                        .setDeliverableName(resultSet.getString("nombre_entregable"))
                        .setStatus(resultSet.getString("estado"))
                        .build());
                }
                return list;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error consultando las autoevaluaciones únicas.");
        }
    }

    public void enableDeliverablesMasive(String nombreDoc, int idExperiencia, java.sql.Date fechaLimite) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Autoevaluacion SET estado = 'Habilitado', fecha_limite = ? WHERE nombre_entregable = ? AND id_asignacion IN (SELECT id_asignacion FROM Asignacion WHERE id_experiencia = ?)")
        ) {
            statement.setDate(1, fechaLimite);
            statement.setString(2, nombreDoc);
            statement.setInt(3, idExperiencia);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error habilitando autoevaluaciones.");
        }
    }

private SelfAssessmentDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new SelfAssessmentDTO.SelfAssessmentBuilder()
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
