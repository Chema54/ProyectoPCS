package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.EvaluacionOVDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EvaluacionOVDAO extends MoldeDAOCompleto<EvaluacionOVDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(EvaluacionOVDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, archivo, estado, fecha_limite, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM Evaluacion_OV";

    private static final String GET_QUERY
            = "SELECT * FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";

    private static final String UPDATE_QUERY
            = "UPDATE Evaluacion_OV SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_limite = ?, calificacion = ?, comentarios = ? WHERE id_evaluacion_ov = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM Evaluacion_OV WHERE id_evaluacion_ov = ?";

    private static final String BATCH_INSERT_QUERY
            = "INSERT INTO Evaluacion_OV (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(EvaluacionOVDTO evaluacionDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, evaluacionDTO.getAsignacionId());
            statement.setString(2, evaluacionDTO.getNombreEntregable());
            statement.setBytes(3, evaluacionDTO.getArchivo());
            statement.setString(4, evaluacionDTO.getEstado() != null ? evaluacionDTO.getEstado() : "Pendiente");
            statement.setDate(5, evaluacionDTO.getFechaLimite());
            statement.setBigDecimal(6, evaluacionDTO.getPuntaje());
            statement.setString(7, evaluacionDTO.getComentarios());

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
                statement.setString(2, "Primera evaluación de la OV");
                statement.executeUpdate();
                statement.setInt(1, asignacionId);
                statement.setString(2, "Segunda evaluación de la OV");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<EvaluacionOVDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<EvaluacionOVDTO> evaluaciones = new ArrayList<>();

            while (resultSet.next()) {
                evaluaciones.add(mapResultSetToDTO(resultSet));
            }

            return evaluaciones;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public EvaluacionOVDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
    public void updateOne(EvaluacionOVDTO evaluacionDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, evaluacionDTO.getAsignacionId());
            statement.setString(2, evaluacionDTO.getNombreEntregable());
            statement.setBytes(3, evaluacionDTO.getArchivo());
            statement.setString(4, evaluacionDTO.getEstado());
            statement.setDate(5, evaluacionDTO.getFechaLimite());
            statement.setBigDecimal(6, evaluacionDTO.getPuntaje());
            statement.setString(7, evaluacionDTO.getComentarios());
            statement.setInt(8, evaluacionDTO.getEvaluacionId());

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

    public List<EvaluacionOVDTO> getAllByAssignmentId(int asignacionId) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM Evaluacion_OV WHERE id_asignacion = ?")) {
            statement.setInt(1, asignacionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<EvaluacionOVDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando las evaluaciones.");
        }
    }

    public List<EvaluacionOVDTO> getUniqueEntregablesByExperiencia(int idExperiencia) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT r.nombre_entregable, r.estado FROM Evaluacion_OV r INNER JOIN Asignacion a ON r.id_asignacion = a.id_asignacion WHERE a.id_experiencia = ?")) {
            statement.setInt(1, idExperiencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<EvaluacionOVDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new EvaluacionOVDTO.EvaluacionOVBuilder()
                            .setNombreEntregable(resultSet.getString("nombre_entregable"))
                            .setEstado(resultSet.getString("estado"))
                            .build());
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando las evaluaciones únicas.");
        }
    }

    public void enableEntregablesMasive(String nombreDoc, int idExperiencia, java.sql.Date fechaLimite) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement("UPDATE Evaluacion_OV SET estado = 'Habilitado', fecha_limite = ? WHERE nombre_entregable = ? AND id_asignacion IN (SELECT id_asignacion FROM Asignacion WHERE id_experiencia = ?)")) {
            statement.setDate(1, fechaLimite);
            statement.setString(2, nombreDoc);
            statement.setInt(3, idExperiencia);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error habilitando evaluaciones.");
        }
    }

    private EvaluacionOVDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new EvaluacionOVDTO.EvaluacionOVBuilder()
                .setEvaluacionId(resultSet.getInt("id_evaluacion_ov"))
                .setAsignacionId(resultSet.getInt("id_asignacion"))
                .setNombreEntregable(resultSet.getString("nombre_entregable"))
                .setArchivo(resultSet.getBytes("archivo"))
                .setEstado(resultSet.getString("estado"))
                .setFechaLimite(resultSet.getDate("fecha_limite"))
                .setPuntaje(resultSet.getBigDecimal("calificacion"))
                .setComentarios(resultSet.getString("comentarios"))
                .build();
    }
}
