package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.AsignacionDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsignacionDAO extends MoldeDAOCompleto<AsignacionDTO, Integer> {

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
    public void createOne(AsignacionDTO assignmentDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT_QUERY)
        ) {
            statement.setInt(1, assignmentDTO.getPracticanteId());
            statement.setInt(2, assignmentDTO.getProyectoId());
            statement.setInt(3, assignmentDTO.getExperienciaEducativaId());
            statement.setString(4, assignmentDTO.getEstado() != null ? assignmentDTO.getEstado() : "Activa");

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public int createOneAndReturnId(AsignacionDTO assignmentDTO) throws ExcepcionMostrableUsuario {
        int generatedId = -1;
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_ASSIGNMENT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, assignmentDTO.getPracticanteId());
            statement.setInt(2, assignmentDTO.getProyectoId());
            statement.setInt(3, assignmentDTO.getExperienciaEducativaId());
            statement.setString(4, assignmentDTO.getEstado() != null ? assignmentDTO.getEstado() : "Activa");

            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID de la asignacion generada.");
                }
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
        return generatedId;
    }
    
    public boolean hasActiveAssignment(int practicanteId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_ACTIVE_ASSIGNMENT_QUERY)
        ) {
            statement.setInt(1, practicanteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
        return false;
    }

    public List<Integer> getAsignacionIdsByPeriod(int periodoId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ASSIGNMENTS_BY_PERIOD_QUERY)
        ) {
            statement.setInt(1, periodoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Integer> assignmentIds = new ArrayList<>();
                while (resultSet.next()) {
                    assignmentIds.add(resultSet.getInt("id_asignacion"));
                }
                return assignmentIds;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public List<Integer> getProyectoIdsByPeriod(int periodoId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_PROJECT_IDS_BY_PERIOD_QUERY)
        ) {
            statement.setInt(1, periodoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Integer> projectIds = new ArrayList<>();
                while (resultSet.next()) {
                    projectIds.add(resultSet.getInt("id_proyecto"));
                }
                return projectIds;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<AsignacionDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<AsignacionDTO> assignments = new ArrayList<>();

            while (resultSet.next()) {
                assignments.add(mapResultSetToDTO(resultSet));
            }

            return assignments;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public AsignacionDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(AsignacionDTO assignmentDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, assignmentDTO.getPracticanteId());
            statement.setInt(2, assignmentDTO.getProyectoId());
            statement.setInt(3, assignmentDTO.getExperienciaEducativaId());
            statement.setString(4, assignmentDTO.getEstado());
            statement.setInt(5, assignmentDTO.getAsignacionId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }
    
    public AsignacionDTO getActiveAssignmentByIntern(int practicanteId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT a.*, p.nombre as p_nombre, p.apellido_paterno as p_ap, p.apellido_materno as p_am, p.matricula, pr.nombre as pr_nombre, e.nrc FROM Asignacion a INNER JOIN Practicante p ON a.id_practicante = p.id_practicante INNER JOIN Proyecto pr ON a.id_proyecto = pr.id_proyecto INNER JOIN ExperienciaEducativa e ON a.id_experiencia = e.id_experiencia WHERE a.id_practicante = ? AND a.estado = 'Activa'")
        ) {
            statement.setInt(1, practicanteId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error obteniendo la asignación activa.");
        }
        return null;
    }

    private AsignacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new AsignacionDTO.AsignacionBuilder()
            .setAsignacionId(resultSet.getInt("id_asignacion"))
            .setPracticanteId(resultSet.getInt("id_practicante"))
            .setProyectoId(resultSet.getInt("id_proyecto"))
            .setExperienciaEducativaId(resultSet.getInt("id_experiencia"))
            .setEstado(resultSet.getString("estado"))
            .setNombreProyecto(resultSet.getString("pr_nombre"))
            .setNombrePracticante(resultSet.getString("p_nombre") + " " + resultSet.getString("p_ap") + " " + resultSet.getString("p_am"))
            .setMatriculaPracticante(resultSet.getString("matricula"))
            .setNrc(resultSet.getString("nrc"))
            .build();
    }
}
