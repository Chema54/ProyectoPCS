package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ReporteMensualDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReporteMensualDAO extends CompleteDAOShape<ReporteMensualDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ReporteMensualDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Reporte_Mensual (id_asignacion, archivo, estado, fecha_entrega, fecha_limite, horas_reportadas, calificacion, comentarios) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Reporte_Mensual";

    private static final String GET_QUERY =
            "SELECT * FROM Reporte_Mensual WHERE id_reporte = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Reporte_Mensual SET id_asignacion = ?, archivo = ?, estado = ?, fecha_entrega = ?, fecha_limite = ?, horas_reportadas = ?, calificacion = ?, comentarios = ? WHERE id_reporte = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Reporte_Mensual WHERE id_reporte = ?";
            
    private static final String CREATE_SHELL_QUERY =
            "INSERT INTO Reporte_Mensual (id_asignacion, estado) VALUES (?, 'Inhabilitado')";

    @Override
    public void createOne(ReporteMensualDTO reportDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, reportDTO.getAssignmentId());
            statement.setString(2, reportDTO.getFile());
            statement.setString(3, reportDTO.getStatus() != null ? reportDTO.getStatus() : "Pendiente");
            statement.setDate(4, reportDTO.getDeliveryDate());
            statement.setDate(5, reportDTO.getDeadline());
            statement.setInt(6, reportDTO.getReportedHours());
            statement.setBigDecimal(7, reportDTO.getScore());
            statement.setString(8, reportDTO.getComments());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del reporte mensual, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la generación del cascarón de reporte, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public List<ReporteMensualDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ReporteMensualDTO> reports = new ArrayList<>();

            while (resultSet.next()) {
                reports.add(new ReporteMensualDTO.ReporteMensualBuilder()
                    .setMonthlyReportId(resultSet.getInt("id_reporte"))
                    .setAssignmentId(resultSet.getInt("id_asignacion"))
                    .setFile(resultSet.getString("archivo"))
                    .setStatus(resultSet.getString("estado"))
                    .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                    .setDeadline(resultSet.getDate("fecha_limite"))
                    .setReportedHours(resultSet.getInt("horas_reportadas"))
                    .setScore(resultSet.getBigDecimal("calificacion"))
                    .setComments(resultSet.getString("comentarios"))
                    .build()
                );
            }

            return reports;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de reportes mensuales, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public ReporteMensualDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ReporteMensualDTO.ReporteMensualBuilder()
                        .setMonthlyReportId(resultSet.getInt("id_reporte"))
                        .setAssignmentId(resultSet.getInt("id_asignacion"))
                        .setFile(resultSet.getString("archivo"))
                        .setStatus(resultSet.getString("estado"))
                        .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                        .setDeadline(resultSet.getDate("fecha_limite"))
                        .setReportedHours(resultSet.getInt("horas_reportadas"))
                        .setScore(resultSet.getBigDecimal("calificacion"))
                        .setComments(resultSet.getString("comentarios"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda del reporte mensual, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(ReporteMensualDTO reportDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, reportDTO.getAssignmentId());
            statement.setString(2, reportDTO.getFile());
            statement.setString(3, reportDTO.getStatus());
            statement.setDate(4, reportDTO.getDeliveryDate());
            statement.setDate(5, reportDTO.getDeadline());
            statement.setInt(6, reportDTO.getReportedHours());
            statement.setBigDecimal(7, reportDTO.getScore());
            statement.setString(8, reportDTO.getComments());
            statement.setInt(9, reportDTO.getMonthlyReportId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del reporte mensual, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación del reporte mensual, debido a un error de conexión con la Base de datos");
        }
    }
}
