package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.ProfesorDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProfesorDAO extends CompleteDAOShape<ProfesorDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ProfesorDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Profesor (numero_personal, nombre, apellido_paterno, apellido_materno, correo, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Profesor";

    private static final String GET_QUERY =
            "SELECT * FROM Profesor WHERE id_profesor = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Profesor SET numero_personal = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ?, estado = ? WHERE id_profesor = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Profesor WHERE id_profesor = ?";

    @Override
    public void createOne(ProfesorDTO professorDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, professorDTO.getPersonalNumber());
            statement.setString(2, professorDTO.getName());
            statement.setString(3, professorDTO.getPaternalSurname());
            statement.setString(4, professorDTO.getMaternalSurname());
            statement.setString(5, professorDTO.getEmail());
            statement.setString(6, professorDTO.getStatus() != null ? professorDTO.getStatus() : "Activo");
            statement.setInt(7, professorDTO.getUserId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del profesor, debido a un error de conexión con la Base de datos");
        }
    }



    @Override
    public List<ProfesorDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ProfesorDTO> professors = new ArrayList<>();

            while (resultSet.next()) {
                professors.add(mapResultSetToDTO(resultSet));
            }

            return professors;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de profesores, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public ProfesorDTO getOne(Integer id) throws UserDisplayableException {
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
                    LOGGER, e, "No se ha podido realizar la búsqueda del profesor, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(ProfesorDTO professorDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, professorDTO.getPersonalNumber());
            statement.setString(2, professorDTO.getName());
            statement.setString(3, professorDTO.getPaternalSurname());
            statement.setString(4, professorDTO.getMaternalSurname());
            statement.setString(5, professorDTO.getEmail());
            statement.setString(6, professorDTO.getStatus());
            statement.setInt(7, professorDTO.getProfessorId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del profesor, debido a un error de conexión con la Base de datos");
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
                    LOGGER, e, "No se ha podido realizar la eliminación del profesor, debido a un error de conexión con la Base de datos");
        }
    }
    
    private ProfesorDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ProfesorDTO.ProfesorBuilder()
            .setProfessorId(resultSet.getInt("id_profesor"))
            .setPersonalNumber(resultSet.getString("numero_personal"))
            .setName(resultSet.getString("nombre"))
            .setPaternalSurname(resultSet.getString("apellido_paterno"))
            .setMaternalSurname(resultSet.getString("apellido_materno"))
            .setEmail(resultSet.getString("correo"))
            .setStatus(resultSet.getString("estado"))
            .setUserId(resultSet.getInt("id_usuario"))
            .build();
    }
}
