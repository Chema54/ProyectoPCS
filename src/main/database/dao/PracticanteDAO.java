package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.PracticanteDTO;
import main.business.dto.UserDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PracticanteDAO extends CompleteDAOShape<PracticanteDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(PracticanteDAO.class);

    private static final String CREATE_USER_QUERY =
            "INSERT INTO Usuario (username, password, role, access, id_rol) VALUES (?, ?, 'INTERN', 1, 1)";
    
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
        Connection connection = null;
        try {
            connection = DBConnector.getInstance().getConnection();
            connection.setAutoCommit(false);

            String rawPassword = UUID.randomUUID().toString().substring(0, 8);
            String hashedPassword = UserDTO.getGeneratedHashedPassword(rawPassword);
            
            int generatedUserId = 0;
            try (PreparedStatement userStatement = connection.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, internDTO.getEnrollment());
                userStatement.setString(2, hashedPassword);
                userStatement.executeUpdate();
                
                try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedUserId = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Fallo al obtener el ID del usuario generado.");
                    }
                }
            }

            try (PreparedStatement internStatement = connection.prepareStatement(CREATE_INTERN_QUERY)) {
                internStatement.setString(1, internDTO.getName());
                internStatement.setString(2, internDTO.getPaternalSurname());
                internStatement.setString(3, internDTO.getMaternalSurname());
                internStatement.setString(4, internDTO.getEmail());
                internStatement.setString(5, internDTO.getEnrollment());
                internStatement.setString(6, internDTO.getStatus() != null ? internDTO.getStatus() : "Activo");
                internStatement.setInt(7, generatedUserId);
                
                internStatement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.error("Error al revertir la transacción", ex);
                }
            }
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del practicante, debido a un error de conexión con la Base de datos");
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.error("Error al cerrar la conexión", e);
                }
            }
        }
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
                interns.add(new PracticanteDTO.PracticanteBuilder()
                    .setInternId(resultSet.getInt("id_practicante"))
                    .setName(resultSet.getString("nombre"))
                    .setPaternalSurname(resultSet.getString("apellido_paterno"))
                    .setMaternalSurname(resultSet.getString("apellido_materno"))
                    .setEmail(resultSet.getString("correo"))
                    .setEnrollment(resultSet.getString("matricula"))
                    .setStatus(resultSet.getString("estado"))
                    .setUserId(resultSet.getInt("id_usuario"))
                    .build()
                );
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
                    return new PracticanteDTO.PracticanteBuilder()
                        .setInternId(resultSet.getInt("id_practicante"))
                        .setName(resultSet.getString("nombre"))
                        .setPaternalSurname(resultSet.getString("apellido_paterno"))
                        .setMaternalSurname(resultSet.getString("apellido_materno"))
                        .setEmail(resultSet.getString("correo"))
                        .setEnrollment(resultSet.getString("matricula"))
                        .setStatus(resultSet.getString("estado"))
                        .setUserId(resultSet.getInt("id_usuario"))
                        .build();
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
