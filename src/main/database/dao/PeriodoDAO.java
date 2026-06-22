package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
            "INSERT INTO Periodo (nombre, fecha_inicio, fecha_fin, id_coordinador) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT p.*, CONCAT_WS(' ', c.nombre, c.apellido_paterno, c.apellido_materno) as coordinatorName FROM Periodo p LEFT JOIN Coordinador c ON p.id_coordinador = c.id_coordinador";

    private static final String GET_QUERY =
            "SELECT p.*, CONCAT_WS(' ', c.nombre, c.apellido_paterno, c.apellido_materno) as coordinatorName FROM Periodo p LEFT JOIN Coordinador c ON p.id_coordinador = c.id_coordinador WHERE p.id_periodo = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Periodo SET nombre = ?, fecha_inicio = ?, fecha_fin = ?, id_coordinador = ? WHERE id_periodo = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Periodo WHERE id_periodo = ?";

    @Override
    public void createOne(PeriodoDTO periodDTO) throws UserDisplayableException {
        createAndReturnId(periodDTO);
    }

    public Integer createAndReturnId(PeriodoDTO periodDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, periodDTO.getName());
            statement.setDate(2, periodDTO.getStartDate());
            statement.setDate(3, periodDTO.getEndDate());
            
            if (periodDTO.getCoordinatorId() != null) {
                statement.setInt(4, periodDTO.getCoordinatorId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Error al crear el periodo, no se pudo obtener el ID.");
                }
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
                periods.add(mapResultSetToDTO(resultSet));
            }

            return periods;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
                    return mapResultSetToDTO(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
            
            if (periodDTO.getCoordinatorId() != null) {
                statement.setInt(4, periodDTO.getCoordinatorId());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            
            statement.setInt(5, periodDTO.getPeriodId());
            
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

    private PeriodoDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        int coordinatorId = resultSet.getInt("id_coordinador");
        Integer coordIdOrNull = resultSet.wasNull() ? null : coordinatorId;
        
        return new PeriodoDTO.PeriodoBuilder()
            .setPeriodId(resultSet.getInt("id_periodo"))
            .setName(resultSet.getString("nombre"))
            .setStartDate(resultSet.getDate("fecha_inicio"))
            .setEndDate(resultSet.getDate("fecha_fin"))
            .setCoordinatorId(coordIdOrNull)
            .setCoordinatorName(resultSet.getString("coordinatorName") != null ? resultSet.getString("coordinatorName").trim() : "Sin asignar")
            .build();
    }
}
