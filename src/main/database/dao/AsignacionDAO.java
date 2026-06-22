package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.AsignacionDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsignacionDAO extends CompleteDAOShape<AsignacionDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(AsignacionDAO.class);
    
    

    private static final String CREATE_ASSIGNMENT_QUERY =
            "INSERT INTO Asignacion (id_practicante, id_proyecto, id_experiencia, estado) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT a.*, p.nombre as p_nombre, p.apellido_paterno as p_ap, p.apellido_materno as p_am, p.matricula, pr.nombre as pr_nombre, e.nrc FROM Asignacion a INNER JOIN Practicante p ON a.id_practicante = p.id_practicante INNER JOIN Proyecto pr ON a.id_proyecto = pr.id_proyecto INNER JOIN ExperienciaEducativa e ON a.id_experiencia = e.id_experiencia";

    private static final String GET_QUERY =
            "SELECT a.*, p.nombre as p_nombre, p.apellido_paterno as p_ap, p.apellido_materno as p_am, p.matricula, pr.nombre as pr_nombre, e.nrc FROM Asignacion a INNER JOIN Practicante p ON a.id_practicante = p.id_practicante INNER JOIN Proyecto pr ON a.id_proyecto = pr.id_proyecto INNER JOIN ExperienciaEducativa e ON a.id_experiencia = e.id_experiencia WHERE a.id_asignacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Asignacion SET id_practicante = ?, id_proyecto = ?, id_experiencia = ?, estado = ? WHERE id_asignacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Asignacion WHERE id_asignacion = ?";
            
    private static final String CHECK_ACTIVE_ASSIGNMENT_QUERY =
            "SELECT COUNT(*) FROM Asignacion WHERE id_practicante = ? AND estado = 'Activa'";
            
    private static final String GET_ASSIGNMENTS_BY_PERIOD_QUERY =
            "SELECT a.id_asignacion FROM Asignacion a INNER JOIN ExperienciaEducativa ee ON a.id_experiencia = ee.id_experiencia WHERE ee.id_periodo = ?";

    private static final String GET_PROJECT_IDS_BY_PERIOD_QUERY =
            "SELECT DISTINCT a.id_proyecto FROM Asignacion a INNER JOIN ExperienciaEducativa ee ON a.id_experiencia = ee.id_experiencia WHERE ee.id_periodo = ?";

    @Override
    public void createOne(AsignacionDTO assignmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT_QUERY)
        ) {
            statement.setInt(1, assignmentDTO.getInternId());
            statement.setInt(2, assignmentDTO.getProjectId());
            statement.setInt(3, assignmentDTO.getEducationalExperienceId());
            statement.setString(4, assignmentDTO.getStatus() != null ? assignmentDTO.getStatus() : "Activa");

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public int createOneAndReturnId(AsignacionDTO assignmentDTO) throws UserDisplayableException {
        int generatedId = -1;
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, assignmentDTO.getInternId());
            statement.setInt(2, assignmentDTO.getProjectId());
            statement.setInt(3, assignmentDTO.getEducationalExperienceId());
            statement.setString(4, assignmentDTO.getStatus() != null ? assignmentDTO.getStatus() : "Activa");

            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID de la asignacion generada.");
                }
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
        return generatedId;
    }
    
    public boolean hasActiveAssignment(int internId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_ACTIVE_ASSIGNMENT_QUERY)
        ) {
            statement.setInt(1, internId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
        return false;
    }

    public List<Integer> getAssignmentIdsByPeriod(int periodId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ASSIGNMENTS_BY_PERIOD_QUERY)
        ) {
            statement.setInt(1, periodId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Integer> assignmentIds = new ArrayList<>();
                while (resultSet.next()) {
                    assignmentIds.add(resultSet.getInt("id_asignacion"));
                }
                return assignmentIds;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public List<Integer> getProjectIdsByPeriod(int periodId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_PROJECT_IDS_BY_PERIOD_QUERY)
        ) {
            statement.setInt(1, periodId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Integer> projectIds = new ArrayList<>();
                while (resultSet.next()) {
                    projectIds.add(resultSet.getInt("id_proyecto"));
                }
                return projectIds;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<AsignacionDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<AsignacionDTO> assignments = new ArrayList<>();

            while (resultSet.next()) {
                assignments.add(mapResultSetToDTO(resultSet));
            }

            return assignments;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public AsignacionDTO getOne(Integer id) throws UserDisplayableException {
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
    public void updateOne(AsignacionDTO assignmentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, assignmentDTO.getInternId());
            statement.setInt(2, assignmentDTO.getProjectId());
            statement.setInt(3, assignmentDTO.getEducationalExperienceId());
            statement.setString(4, assignmentDTO.getStatus());
            statement.setInt(5, assignmentDTO.getAssignmentId());
            
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
    
    private AsignacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new AsignacionDTO.AsignacionBuilder()
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setInternId(resultSet.getInt("id_practicante"))
            .setProjectId(resultSet.getInt("id_proyecto"))
            .setEducationalExperienceId(resultSet.getInt("id_experiencia"))
            .setStatus(resultSet.getString("estado"))
            .setProjectName(resultSet.getString("pr_nombre"))
            .setPracticanteName(resultSet.getString("p_nombre") + " " + resultSet.getString("p_ap") + " " + resultSet.getString("p_am"))
            .setPracticanteMatricula(resultSet.getString("matricula"))
            .setNrc(resultSet.getString("nrc"))
            .build();
    }
}
