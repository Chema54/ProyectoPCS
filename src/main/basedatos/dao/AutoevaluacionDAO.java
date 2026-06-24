package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.AutoevaluacionDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AutoevaluacionDAO extends MoldeDAOCompleto<AutoevaluacionDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(AutoevaluacionDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Autoevaluacion (id_asignacion, nombre_entregable, archivo, calificacion, comentarios, estado, fecha_limite) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM Autoevaluacion";

    private static final String GET_QUERY
            = "SELECT * FROM Autoevaluacion WHERE id_autoevaluacion = ?";

    private static final String UPDATE_QUERY
            = "UPDATE Autoevaluacion SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, calificacion = ?, comentarios = ?, estado = ?, fecha_limite = ? WHERE id_autoevaluacion = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM Autoevaluacion WHERE id_autoevaluacion = ?";

    private static final String BATCH_INSERT_QUERY
            = "INSERT INTO Autoevaluacion (id_asignacion, nombre_entregable, estado) VALUES (?, 'Autoevaluación del estudiante', 'Inhabilitado')";

    @Override
    public void createOne(AutoevaluacionDTO autoevaluacionDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, autoevaluacionDTO.getAsignacionId());
            statement.setString(2, autoevaluacionDTO.getNombreEntregable());
            statement.setBytes(3, autoevaluacionDTO.getArchivo());
            statement.setBigDecimal(4, autoevaluacionDTO.getPuntaje());
            statement.setString(5, autoevaluacionDTO.getComentarios());
            statement.setString(6, autoevaluacionDTO.getEstado() != null ? autoevaluacionDTO.getEstado() : "Pendiente");
            statement.setDate(7, autoevaluacionDTO.getFechaLimite());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void createEntregables(List<Integer> assignmentIds) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)) {
            for (Integer asignacionId : assignmentIds) {
                statement.setInt(1, asignacionId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<AutoevaluacionDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<AutoevaluacionDTO> autoevaluaciones = new ArrayList<>();

            while (resultSet.next()) {
                autoevaluaciones.add(mapResultSetToDTO(resultSet));
            }

            return autoevaluaciones;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public AutoevaluacionDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(AutoevaluacionDTO autoevaluacionDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, autoevaluacionDTO.getAsignacionId());
            statement.setString(2, autoevaluacionDTO.getNombreEntregable());
            statement.setBytes(3, autoevaluacionDTO.getArchivo());
            statement.setBigDecimal(4, autoevaluacionDTO.getPuntaje());
            statement.setString(5, autoevaluacionDTO.getComentarios());
            statement.setString(6, autoevaluacionDTO.getEstado());
            statement.setDate(7, autoevaluacionDTO.getFechaLimite());
            statement.setInt(8, autoevaluacionDTO.getAutoevaluacionId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void deleteOne(Integer id) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public List<AutoevaluacionDTO> getAllByAssignmentId(int asignacionId) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM Autoevaluacion WHERE id_asignacion = ?")) {
            statement.setInt(1, asignacionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<AutoevaluacionDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando las autoevaluaciones.");
        }
    }

    public List<AutoevaluacionDTO> getUniqueEntregablesByExperiencia(int idExperiencia) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT r.nombre_entregable, r.estado FROM Autoevaluacion r INNER JOIN Asignacion a ON r.id_asignacion = a.id_asignacion WHERE a.id_experiencia = ?")) {
            statement.setInt(1, idExperiencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<AutoevaluacionDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new AutoevaluacionDTO.AutoevaluacionBuilder()
                            .setNombreEntregable(resultSet.getString("nombre_entregable"))
                            .setEstado(resultSet.getString("estado"))
                            .build());
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando las autoevaluaciones únicas.");
        }
    }

    public void enableEntregablesMasive(String nombreDoc, int idExperiencia, java.sql.Date fechaLimite) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE Autoevaluacion SET estado = 'Habilitado', fecha_limite = ? WHERE nombre_entregable = ? AND id_asignacion IN (SELECT id_asignacion FROM Asignacion WHERE id_experiencia = ?)")) {
            statement.setDate(1, fechaLimite);
            statement.setString(2, nombreDoc);
            statement.setInt(3, idExperiencia);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error habilitando autoevaluaciones.");
        }
    }

    private AutoevaluacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new AutoevaluacionDTO.AutoevaluacionBuilder()
                .setAutoevaluacionId(resultSet.getInt("id_autoevaluacion"))
                .setAsignacionId(resultSet.getInt("id_asignacion"))
                .setNombreEntregable(resultSet.getString("nombre_entregable"))
                .setArchivo(resultSet.getBytes("archivo"))
                .setPuntaje(resultSet.getBigDecimal("calificacion"))
                .setComentarios(resultSet.getString("comentarios"))
                .setEstado(resultSet.getString("estado"))
                .setFechaLimite(resultSet.getDate("fecha_limite"))
                .build();
    }
}
