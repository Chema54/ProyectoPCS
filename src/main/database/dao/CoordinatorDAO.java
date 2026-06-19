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
import main.business.dto.CoordinatorDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import main.database.dao.shape.CompleteDAOShape;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class CoordinatorDAO extends CompleteDAOShape<CoordinatorDTO, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(CoordinatorDTO.class);
    
    private static final String CREATE_QUERY = 
        "INSERT INTO Coordinador (id_usuario, numeroPersonal, nombre, apellido_paterno, apellido_materno, correo) VALUES (?, ?, ?, ?, ?, ?)";    
    private static final String GET_ALL_QUERY = 
        "SELECT c.id_coordinador, c.id_usuario, u.username, " +
        "c.numeroPersonal, c.nombre, c.apellido_paterno, c.apellido_materno, c.correo " +
        "FROM Coordinador c " +
        "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario";
    private static final String GET_QUERY = 
        "SELECT c.id_coordinador, c.id_usuario, u.username, " +
        "c.numeroPersonal, c.nombre, c.apellido_paterno, c.apellido_materno, c.correo " +
        "FROM Coordinador c " +
        "INNER JOIN Usuario u ON c.id_usuario = u.id_usuario " +
        "WHERE c.id_coordinador = ?";
    private static final String UPDATE_QUERY = 
        "UPDATE Coordinador " +
        "SET id_usuario = ?, numeroPersonal = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ? " +
        "WHERE id_coordinador = ?";
      private static final String DELETE_QUERY = 
        "DELETE FROM Coordinador WHERE id_coordinador = ?";

    @Override
    public void createOne(CoordinatorDTO coordinatorDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, coordinatorDTO.getIDUser());
            statement.setString(2, coordinatorDTO.getAcademicNumber());
            statement.setString(3, coordinatorDTO.getNombre());
            statement.setString(4, coordinatorDTO.getLastName()); 
            statement.setString(5, coordinatorDTO.getMotherLastName());
            statement.setString(6, coordinatorDTO.getEmail());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                LOGGER, e, "No ha sido posible registrar el coordinador."
            );
        }
    }
    
    @Override
    public List<CoordinatorDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<CoordinatorDTO> coordinators = new ArrayList<>();
            while (resultSet.next()) {
                CoordinatorDTO coordinator = new CoordinatorDTO.CoordinatorBuilder()
                    .setIDCoordinator(resultSet.getInt("id_coordinador"))
                    .setIDUser(resultSet.getInt("id_usuario"))
                    .setUsername(resultSet.getString("username"))
                    .setAcademicNumber(resultSet.getString("numeroPersonal"))
                    .setName(resultSet.getString("nombre"))
                    .setLastName(resultSet.getString("apellido_paterno"))
                    .setMotherLastName(resultSet.getString("apellido_materno"))
                    .build();
                coordinators.add(coordinator);
            }
            return coordinators;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                LOGGER, e, "No ha sido posible cargar los coordinadores."
            );
        }
    }

    @Override
    public CoordinatorDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new CoordinatorDTO.CoordinatorBuilder()
                        .setIDCoordinator(resultSet.getInt("id_coordinador"))
                        .setIDUser(resultSet.getInt("id_usuario"))
                        .setUsername(resultSet.getString("username"))    
                        .setAcademicNumber(resultSet.getString("numeroPersonal"))
                        .setName(resultSet.getString("nombre"))
                        .setLastName(resultSet.getString("apellido_paterno"))
                        .setMotherLastName(resultSet.getString("apellido_materno"))
                        .build();
                }
                return null;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                LOGGER, e, "No ha sido posible obtener el coordinador."
            );
        }
    }

    @Override
    public void updateOne(CoordinatorDTO coordinatorDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {

            statement.setInt(1, coordinatorDTO.getIDUser());
            statement.setString(2, coordinatorDTO.getAcademicNumber());
            statement.setString(3, coordinatorDTO.getNombre());
            statement.setInt(4, coordinatorDTO.getIDCoordinator());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                LOGGER, e, "No ha sido posible actualizar el coordinador."
            );
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
                LOGGER, e, "No ha sido posible eliminar el coordinador."
            );
        }
    }

    
}