package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.PeriodoDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PeriodoDAO extends CompleteDAOShape<PeriodoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(PeriodoDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Periodo (nombre, fecha_inicio, fecha_fin) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Periodo";

    private static final String GET_QUERY =
            "SELECT * FROM Periodo WHERE id_periodo = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Periodo SET nombre = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_periodo = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Periodo WHERE id_periodo = ?";

    @Override
    public void createOne(PeriodoDTO periodDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, periodDTO.getName());
            statement.setDate(2, periodDTO.getStartDate());
            statement.setDate(3, periodDTO.getEndDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del periodo, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public List<PeriodoDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<PeriodoDTO> periods = new ArrayList<>();

            while (resultSet.next()) {
                periods.add(new PeriodoDTO.PeriodoBuilder()
                    .setPeriodId(resultSet.getInt("id_periodo"))
                    .setName(resultSet.getString("nombre"))
                    .setStartDate(resultSet.getDate("fecha_inicio"))
                    .setEndDate(resultSet.getDate("fecha_fin"))
                    .build()
                );
            }

            return periods;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de periodos, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public PeriodoDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new PeriodoDTO.PeriodoBuilder()
                        .setPeriodId(resultSet.getInt("id_periodo"))
                        .setName(resultSet.getString("nombre"))
                        .setStartDate(resultSet.getDate("fecha_inicio"))
                        .setEndDate(resultSet.getDate("fecha_fin"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda del periodo, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(PeriodoDTO periodDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, periodDTO.getName());
            statement.setDate(2, periodDTO.getStartDate());
            statement.setDate(3, periodDTO.getEndDate());
            statement.setInt(4, periodDTO.getPeriodId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del periodo, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación del periodo, debido a un error de conexión con la Base de datos");
        }
    }
}
