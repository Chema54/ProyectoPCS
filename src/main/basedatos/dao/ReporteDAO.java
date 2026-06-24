package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.ReporteDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReporteDAO extends MoldeDAOCompleto<ReporteDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ReporteDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Reporte (id_asignacion, nombre_entregable, archivo, estado, fecha_limite, horas_reportadas, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Reporte";

    private static final String GET_QUERY =
            "SELECT * FROM Reporte WHERE id_reporte = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Reporte SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_limite = ?, horas_reportadas = ?, calificacion = ?, comentarios = ? WHERE id_reporte = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Reporte WHERE id_reporte = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Reporte (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(ReporteDTO reporteDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, reporteDTO.getAsignacionId());
            statement.setString(2, reporteDTO.getNombreEntregable());
            statement.setBytes(3, reporteDTO.getArchivo());
            statement.setString(4, reporteDTO.getEstado() != null ? reporteDTO.getEstado() : "Pendiente");
            statement.setDate(5, reporteDTO.getFechaLimite());
            statement.setInt(6, reporteDTO.getHorasReportadas());
            statement.setBigDecimal(7, reporteDTO.getPuntaje());
            statement.setString(8, reporteDTO.getComentarios());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void createEntregables(List<Integer> assignmentIds) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)
        ) {
            for (Integer asignacionId : assignmentIds) {
                for (int i = 1; i <= 4; i++) {
                    statement.setInt(1, asignacionId);
                    statement.setString(2, "Reporte mensual " + i);
                    statement.executeUpdate();
                }
                
                statement.setInt(1, asignacionId);
                statement.setString(2, "Primer informe 210 hrs");
                statement.executeUpdate();
                
                statement.setInt(1, asignacionId);
                statement.setString(2, "Segundo informe 420 hrs.");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<ReporteDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ReporteDTO> reportes = new ArrayList<>();

            while (resultSet.next()) {
                reportes.add(mapResultSetToDTO(resultSet));
            }

            return reportes;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public ReporteDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
    public void updateOne(ReporteDTO reporteDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, reporteDTO.getAsignacionId());
            statement.setString(2, reporteDTO.getNombreEntregable());
            statement.setBytes(3, reporteDTO.getArchivo());
            statement.setString(4, reporteDTO.getEstado());
            statement.setDate(5, reporteDTO.getFechaLimite());
            statement.setInt(6, reporteDTO.getHorasReportadas());
            statement.setBigDecimal(7, reporteDTO.getPuntaje());
            statement.setString(8, reporteDTO.getComentarios());
            statement.setInt(9, reporteDTO.getReporteId());
            
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
    
    public List<ReporteDTO> getAllByAssignmentId(int asignacionId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Reporte WHERE id_asignacion = ?")
        ) {
            statement.setInt(1, asignacionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<ReporteDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando los reportes.");
        }
    }

        public List<ReporteDTO> getUniqueEntregablesByExperiencia(int idExperiencia) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT r.nombre_entregable, r.estado FROM Reporte r INNER JOIN Asignacion a ON r.id_asignacion = a.id_asignacion WHERE a.id_experiencia = ?")
        ) {
            statement.setInt(1, idExperiencia);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<ReporteDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(new ReporteDTO.ReporteBuilder()
                        .setNombreEntregable(resultSet.getString("nombre_entregable"))
                        .setEstado(resultSet.getString("estado"))
                        .build());
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando los reportes únicos.");
        }
    }

    public void enableEntregablesMasive(String nombreDoc, int idExperiencia, java.sql.Date fechaLimite) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE Reporte SET estado = 'Habilitado', fecha_limite = ? WHERE nombre_entregable = ? AND id_asignacion IN (SELECT id_asignacion FROM Asignacion WHERE id_experiencia = ?)")
        ) {
            statement.setDate(1, fechaLimite);
            statement.setString(2, nombreDoc);
            statement.setInt(3, idExperiencia);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error habilitando reportes.");
        }
    }

private ReporteDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ReporteDTO.ReporteBuilder()
            .setReporteId(resultSet.getInt("id_reporte"))
            .setAsignacionId(resultSet.getInt("id_asignacion"))
            .setNombreEntregable(resultSet.getString("nombre_entregable"))
            .setArchivo(resultSet.getBytes("archivo"))
            .setEstado(resultSet.getString("estado"))
            .setFechaLimite(resultSet.getDate("fecha_limite"))
            .setHorasReportadas(resultSet.getInt("horas_reportadas"))
            .setPuntaje(resultSet.getBigDecimal("calificacion"))
            .setComentarios(resultSet.getString("comentarios"))
            .build();
    }
}
