package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.PracticanteDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PracticanteDAO extends CompleteDAOShape<PracticanteDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(PracticanteDAO.class);

    private static final String CREATE_INTERN_QUERY =
            "INSERT INTO Practicante (nombre, apellido_paterno, apellido_materno, correo, matricula, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Practicante";

    private static final String GET_QUERY =
            "SELECT * FROM Practicante WHERE id_practicante = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Practicante SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ?, matricula = ?, estado = ? WHERE id_practicante = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Practicante WHERE id_practicante = ?";

    @Override
    public void createOne(PracticanteDTO internDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement internStatement = connection.prepareStatement(CREATE_INTERN_QUERY)
        ) {
            internStatement.setString(1, internDTO.getName());
            internStatement.setString(2, internDTO.getPaternalSurname());
            internStatement.setString(3, internDTO.getMaternalSurname());
            internStatement.setString(4, internDTO.getEmail());
            internStatement.setString(5, internDTO.getEnrollment());
            internStatement.setString(6, internDTO.getStatus() != null ? internDTO.getStatus() : "Activo");
            internStatement.setInt(7, internDTO.getUserId());
            
            internStatement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    public int createOneAndReturnId(PracticanteDTO internDTO) throws UserDisplayableException {
        int generatedId = -1;
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement internStatement = connection.prepareStatement(CREATE_INTERN_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            internStatement.setString(1, internDTO.getName());
            internStatement.setString(2, internDTO.getPaternalSurname());
            internStatement.setString(3, internDTO.getMaternalSurname());
            internStatement.setString(4, internDTO.getEmail());
            internStatement.setString(5, internDTO.getEnrollment());
            internStatement.setString(6, internDTO.getStatus() != null ? internDTO.getStatus() : "Activo");
            internStatement.setInt(7, internDTO.getUserId());
            
            internStatement.executeUpdate();
            
            try (ResultSet generatedKeys = internStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del practicante generado.");
                }
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del practicante, debido a un error de conexión con la Base de datos");
        }
        return generatedId;
    }

    @Override
    public List<PracticanteDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<PracticanteDTO> interns = new ArrayList<>();

            while (resultSet.next()) {
                interns.add(mapResultSetToDTO(resultSet));
            }

            return interns;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de practicantes, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public PracticanteDTO getOne(Integer id) throws UserDisplayableException {
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
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(PracticanteDTO internDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, internDTO.getName());
            statement.setString(2, internDTO.getPaternalSurname());
            statement.setString(3, internDTO.getMaternalSurname());
            statement.setString(4, internDTO.getEmail());
            statement.setString(5, internDTO.getEnrollment());
            statement.setString(6, internDTO.getStatus());
            statement.setInt(7, internDTO.getInternId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del practicante, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación del practicante, debido a un error de conexión con la Base de datos");
        }
    }
}
