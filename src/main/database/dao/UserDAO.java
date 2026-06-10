/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class UserDAO extends CompleteDAOShape<UserDTO, String> {

    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Usuario (id_usuario, username, password, role, access) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Usuario";

    private static final String GET_QUERY =
            "SELECT * FROM Usuario WHERE username = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Usuario SET username = ?, password = ?, role = ?, access = ? WHERE id_usuario = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Usuario WHERE id_usuario = ?";

    @Override
    public void createOne(UserDTO userDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, userDTO.getUserID());
            statement.setString(2, userDTO.getUsername());
            statement.setString(3, userDTO.getPassword());
            statement.setString(4, userDTO.getRole().name());
            statement.setBoolean(5, userDTO.hasAccess());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No ha sido posible crear el usuario.");
        }
    }

    @Override
    public List<UserDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<UserDTO> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(new UserDTO.UserBuilder()
                    .setUserID(resultSet.getInt("id_usuario"))
                    .setUsername(resultSet.getString("username"))
                    .setPassword(resultSet.getString("password"))
                    .setRole(UserRole.fromId(resultSet.getInt("id_rol")))
                    .setAccess(resultSet.getBoolean("access"))
                    .build()
                );
            }

            return users;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No ha sido posible cargar los usuarios.");
        }
    }

    @Override
    public UserDTO getOne(String username) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new UserDTO.UserBuilder()
                    .setUserID(resultSet.getInt("id_usuario"))
                    .setUsername(resultSet.getString("username"))
                    .setPassword(resultSet.getString("password"))
                    .setRole(UserRole.fromId(resultSet.getInt("id_rol")))
                    .setAccess(resultSet.getBoolean("access"))
                    .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible obtener el usuario.");
        }
    }

    @Override
    public void updateOne(UserDTO userDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, userDTO.getUsername());
            statement.setString(2, userDTO.getPassword());
            statement.setString(3, userDTO.getRole().name());
            statement.setBoolean(4, userDTO.hasAccess());
            statement.setInt(5, userDTO.getUserID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible actualizar el usuario.");
        }
    }

    @Override
    public void deleteOne(String id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar el usuario.");
        }
    }
}
