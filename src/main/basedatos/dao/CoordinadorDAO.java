/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.negocio.dto.CoordinadorDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class CoordinadorDAO extends MoldeDAOCompleto<CoordinadorDTO, Integer> {
    private static final Logger LOGGER = LogManager.getLogger(CoordinadorDTO.class);
    
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
    public void createOne(CoordinadorDTO coordinatorDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, coordinatorDTO.getIDUser());
            statement.setString(2, coordinatorDTO.getNumeroPersonal());
            statement.setString(3, coordinatorDTO.getNombre());
            statement.setString(4, coordinatorDTO.getLastName()); 
            statement.setString(5, coordinatorDTO.getMotherLastName());
            statement.setString(6, coordinatorDTO.getCorreo());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                LOGGER, e, "No ha sido posible registrar el coordinador."
            );
        }
    }
    
    @Override
    public List<CoordinadorDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<CoordinadorDTO> coordinators = new ArrayList<>();
            while (resultSet.next()) {
                coordinators.add(mapResultSetToDTO(resultSet));
            }
            return coordinators;
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                LOGGER, e, "No ha sido posible cargar los coordinadores."
            );
        }
    }

    @Override
    public CoordinadorDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
            throw ManejadorExcepciones.handleSQLException(
                LOGGER, e, "No ha sido posible obtener el coordinador."
            );
        }
    }

    @Override
    public void updateOne(CoordinadorDTO coordinatorDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {

            statement.setInt(1, coordinatorDTO.getIDUser());
            statement.setString(2, coordinatorDTO.getNumeroPersonal());
            statement.setString(3, coordinatorDTO.getNombre());
            statement.setInt(4, coordinatorDTO.getIDCoordinator());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                LOGGER, e, "No ha sido posible actualizar el coordinador."
            );
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible eliminar el coordinador.");
        }
    }

    private CoordinadorDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new CoordinadorDTO.CoordinadorBuilder()
            .setIDCoordinator(resultSet.getInt("id_coordinador"))
            .setIDUser(resultSet.getInt("id_usuario"))
            .setNombreUsuario(resultSet.getString("username"))
            .setNumeroPersonal(resultSet.getString("numeroPersonal"))
            .setNombre(resultSet.getString("nombre"))
            .setLastName(resultSet.getString("apellido_paterno"))
            .setMotherLastName(resultSet.getString("apellido_materno"))
            .setCorreo(resultSet.getString("correo"))
            .build();
    }
}