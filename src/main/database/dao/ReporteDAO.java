package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ReporteDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReporteDAO extends CompleteDAOShape<ReporteDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ReporteDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Reporte (id_asignacion, nombre_entregable, archivo, estado, fecha_entrega, fecha_limite, horas_reportadas, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Reporte";

    private static final String GET_QUERY =
            "SELECT * FROM Reporte WHERE id_reporte = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Reporte SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_entrega = ?, fecha_limite = ?, horas_reportadas = ?, calificacion = ?, comentarios = ? WHERE id_reporte = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Reporte WHERE id_reporte = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Reporte (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(ReporteDTO reportDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, reportDTO.getAssignmentId());
            statement.setString(2, reportDTO.getDeliverableName());
            statement.setString(3, reportDTO.getFile());
            statement.setString(4, reportDTO.getStatus() != null ? reportDTO.getStatus() : "Pendiente");
            statement.setDate(5, reportDTO.getDeliveryDate());
            statement.setDate(6, reportDTO.getDeadline());
            statement.setInt(7, reportDTO.getReportedHours());
            statement.setBigDecimal(8, reportDTO.getScore());
            statement.setString(9, reportDTO.getComments());

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
                // 3 inserts required per assignment
                statement.setInt(1, assignmentId);
                statement.setString(2, "Reportes mensuales (4)");
                statement.executeUpdate();
                
                statement.setInt(1, assignmentId);
                statement.setString(2, "Primer informe 210 hrs");
                statement.executeUpdate();
                
                statement.setInt(1, assignmentId);
                statement.setString(2, "Segundo informe 420 hrs.");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<ReporteDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ReporteDTO> reports = new ArrayList<>();

            while (resultSet.next()) {
                reports.add(mapResultSetToDTO(resultSet));
            }

            return reports;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public ReporteDTO getOne(Integer id) throws UserDisplayableException {
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
    public void updateOne(ReporteDTO reportDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, reportDTO.getAssignmentId());
            statement.setString(2, reportDTO.getDeliverableName());
            statement.setString(3, reportDTO.getFile());
            statement.setString(4, reportDTO.getStatus());
            statement.setDate(5, reportDTO.getDeliveryDate());
            statement.setDate(6, reportDTO.getDeadline());
            statement.setInt(7, reportDTO.getReportedHours());
            statement.setBigDecimal(8, reportDTO.getScore());
            statement.setString(9, reportDTO.getComments());
            statement.setInt(10, reportDTO.getMonthlyReportId());
            
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
    
    private ReporteDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ReporteDTO.ReporteBuilder()
            .setMonthlyReportId(resultSet.getInt("id_reporte"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getString("archivo"))
            .setStatus(resultSet.getString("estado"))
            .setDeliveryDate(resultSet.getDate("fecha_entrega"))
            .setDeadline(resultSet.getDate("fecha_limite"))
            .setReportedHours(resultSet.getInt("horas_reportadas"))
            .setScore(resultSet.getBigDecimal("calificacion"))
            .setComments(resultSet.getString("comentarios"))
            .build();
    }
}
