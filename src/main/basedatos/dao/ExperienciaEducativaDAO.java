package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.ConexionBD;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.ManejadorExcepciones;
import main.negocio.dto.ExperienciaEducativaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExperienciaEducativaDAO extends MoldeDAOCompleto<ExperienciaEducativaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ExperienciaEducativaDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO ExperienciaEducativa (nombre, id_periodo, nrc) VALUES (?, ?, ?)";

    private static final String CREATE_PROFESSOR_EXPERIENCE_QUERY
            = "INSERT INTO ProfesorExperiencia (id_profesor, id_experiencia) VALUES (?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT e.*, p.nombre AS periodName, "
            + "CONCAT_WS(' ', prof.nombre, prof.apellido_paterno, prof.apellido_materno) AS professorName "
            + "FROM ExperienciaEducativa e "
            + "INNER JOIN Periodo p ON e.id_periodo = p.id_periodo "
            + "LEFT JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia "
            + "LEFT JOIN Profesor prof ON pe.id_profesor = prof.id_profesor";

    private static final String GET_QUERY
            = "SELECT e.*, p.nombre AS periodName, "
            + "CONCAT_WS(' ', prof.nombre, prof.apellido_paterno, prof.apellido_materno) AS professorName "
            + "FROM ExperienciaEducativa e "
            + "INNER JOIN Periodo p ON e.id_periodo = p.id_periodo "
            + "LEFT JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia "
            + "LEFT JOIN Profesor prof ON pe.id_profesor = prof.id_profesor "
            + "WHERE e.id_experiencia = ?";

    private static final String UPDATE_QUERY
            = "UPDATE ExperienciaEducativa SET nombre = ?, id_periodo = ?, nrc = ? WHERE id_experiencia = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM ExperienciaEducativa WHERE id_experiencia = ?";

    private static final String EXISTS_BY_NRC_QUERY
            = "SELECT COUNT(*) FROM ExperienciaEducativa WHERE nrc = ?";

    private static final String EXISTS_BY_NAME_NRC_PERIOD_QUERY
            = "SELECT COUNT(*) FROM ExperienciaEducativa WHERE nombre = ? AND nrc = ? AND id_periodo = ?";

    @Override
    public void createOne(ExperienciaEducativaDTO experienceDTO) throws ExcepcionMostrableUsuario {
        createOneAndReturnId(experienceDTO);
    }

    public int createOneAndReturnId(ExperienciaEducativaDTO experienceDTO) throws ExcepcionMostrableUsuario {
        try (Connection connection = ConexionBD.getInstance().getConnection()) {
            return createExperience(connection, experienceDTO);
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido registrar la experiencia educativa, debido a un error de conexión."
            );
        }
    }

    public int createOneWithProfessor(ExperienciaEducativaDTO experienceDTO, int professorId) throws ExcepcionMostrableUsuario {
        Connection connection = null;

        try {
            connection = ConexionBD.getInstance().getConnection();
            connection.setAutoCommit(false);

            int experienceId = createExperience(connection, experienceDTO);
            assignProfessorToExperience(connection, professorId, experienceId);

            connection.commit();
            return experienceId;

        } catch (SQLException e) {
            rollback(connection);

            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido registrar la experiencia educativa y su profesor, debido a un error de conexión."
            );
        } finally {
            closeConnection(connection);
        }
    }

    private int createExperience(Connection connection, ExperienciaEducativaDTO experienceDTO) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, experienceDTO.getNombre());

            if (experienceDTO.getPeriodoId() != null) {
                statement.setInt(2, experienceDTO.getPeriodoId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, experienceDTO.getNrc());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }

                throw new SQLException("No se pudo obtener el id de la experiencia educativa registrada.");
            }
        }
    }

    private void assignProfessorToExperience(Connection connection, int professorId, int experienceId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_PROFESSOR_EXPERIENCE_QUERY)) {
            statement.setInt(1, professorId);
            statement.setInt(2, experienceId);
            statement.executeUpdate();
        }
    }

    public boolean existsByNrc(String nrc) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(EXISTS_BY_NRC_QUERY)) {
            statement.setString(1, nrc);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido verificar el NRC de la experiencia educativa."
            );
        }
    }

    public boolean existsByNameNrcAndPeriod(String name, String nrc, int periodId) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(EXISTS_BY_NAME_NRC_PERIOD_QUERY)) {
            statement.setString(1, name);
            statement.setString(2, nrc);
            statement.setInt(3, periodId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido verificar si la experiencia educativa ya existe."
            );
        }
    }

    @Override
    public List<ExperienciaEducativaDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<ExperienciaEducativaDTO> experiences = new ArrayList<>();

            while (resultSet.next()) {
                experiences.add(mapResultSetToDTO(resultSet));
            }

            return experiences;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido consultar las experiencias educativas, debido a un error de conexión."
            );
        }
    }

    @Override
    public ExperienciaEducativaDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido buscar la experiencia educativa, debido a un error de conexión."
            );
        }
    }

    @Override
    public void updateOne(ExperienciaEducativaDTO experienceDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, experienceDTO.getNombre());

            if (experienceDTO.getPeriodoId() != null) {
                statement.setInt(2, experienceDTO.getPeriodoId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }

            statement.setString(3, experienceDTO.getNrc());
            statement.setInt(4, experienceDTO.getExperienciaEducativaId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido actualizar la experiencia educativa, debido a un error de conexión."
            );
        }
    }

    @Override
    public void deleteOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido eliminar la experiencia educativa, debido a un error de conexión."
            );
        }
    }

    public List<ExperienciaEducativaDTO> getExperienciasByUserId(int usuarioId) throws ExcepcionMostrableUsuario {
        String query
                = "SELECT e.*, p.nombre AS periodName, "
                + "CONCAT(prof.nombre, ' ', prof.apellido_paterno) AS professorName "
                + "FROM ExperienciaEducativa e "
                + "INNER JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia "
                + "INNER JOIN Profesor prof ON pe.id_profesor = prof.id_profesor "
                + "LEFT JOIN Periodo p ON e.id_periodo = p.id_periodo "
                + "WHERE prof.id_usuario = ?";

        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, usuarioId);

            try (ResultSet resultSet = statement.executeQuery()) {
                List<ExperienciaEducativaDTO> list = new ArrayList<>();

                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }

                return list;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "Error al buscar las experiencias educativas del profesor."
            );
        }
    }

    private ExperienciaEducativaDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        int periodoId = resultSet.getInt("id_periodo");
        Integer periodIdOrNull = resultSet.wasNull() ? null : periodoId;

        String professorName = resultSet.getString("professorName");

        return new ExperienciaEducativaDTO.ExperienciaEducativaBuilder()
                .setExperienciaEducativaId(resultSet.getInt("id_experiencia"))
                .setNombre(resultSet.getString("nombre"))
                .setPeriodoId(periodIdOrNull)
                .setNrc(resultSet.getString("nrc"))
                .setNombrePeriodo(resultSet.getString("periodName"))
                .setNombreProfesor(
                        professorName != null && !professorName.trim().isEmpty()
                        ? professorName.trim()
                        : "Sin asignar"
                )
                .build();
    }

    private void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error("No se pudo revertir la transacción de experiencia educativa.", e);
            }
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("No se pudo cerrar la conexión de experiencia educativa.", e);
            }
        }
    }
}
