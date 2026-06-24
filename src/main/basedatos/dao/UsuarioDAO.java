package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.UsuarioDTO;
import main.negocio.dto.enumeracion.RolUsuario;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author josem
 */
public class UsuarioDAO extends MoldeDAOCompleto<UsuarioDTO, String> {

    private static final Logger LOGGER = LogManager.getLogger(UsuarioDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO Usuario (username, password, role, access, id_rol) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM Usuario";

    private static final String GET_QUERY
            = "SELECT * FROM Usuario WHERE username = ?";

    private static final String UPDATE_QUERY
            = "UPDATE Usuario SET username = ?, password = ?, role = ?, access = ? WHERE id_usuario = ?";

    private static final String DELETE_QUERY
            = "DELETE FROM Usuario WHERE id_usuario = ?";

    @Override
    public void createOne(UsuarioDTO userDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getContrasenia());
            statement.setString(3, userDTO.getRol().name());
            statement.setBoolean(4, userDTO.tieneAcceso());
            statement.setInt(5, userDTO.getRol().getIdRol());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No ha sido posible crear el usuario.");
        }
    }

    public int createOneAndReturnId(UsuarioDTO userDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getContrasenia());
            statement.setString(3, userDTO.getRol().name());
            statement.setBoolean(4, userDTO.tieneAcceso());
            statement.setInt(5, userDTO.getRol().getIdRol());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);

                    try (java.sql.Statement nativeStmt = connection.createStatement()) {
                        String username = userDTO.getNombreUsuario();
                        String password = userDTO.getContrasenia();
                        nativeStmt.execute("CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password + "'");
                        nativeStmt.execute("GRANT ALL PRIVILEGES ON practicas_profesionales.* TO '" + username + "'@'%'");
                    } catch (SQLException ex) {
                        LOGGER.error("No se pudo crear el usuario nativo en MySQL", ex);
                        throw new ExcepcionMostrableUsuario("Error de Base de Datos", "Error creando credenciales", "No se pudieron crear las credenciales nativas.");
                    }

                    return newId;
                } else {
                    throw new SQLException("Fallo al obtener el ID del usuario generado.");
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No ha sido posible crear el usuario.");
        }
    }

    @Override
    public List<UsuarioDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<UsuarioDTO> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(mapResultSetToDTO(resultSet));
            }

            return users;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No ha sido posible cargar los usuarios.");
        }
    }

    @Override
    public UsuarioDTO getOne(String username) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_QUERY)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }
                return null;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible obtener el usuario.");
        }
    }

    @Override
    public void updateOne(UsuarioDTO userDTO) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getContrasenia());
            statement.setString(3, userDTO.getRol().name());
            statement.setBoolean(4, userDTO.tieneAcceso());
            statement.setInt(5, userDTO.getUsuarioId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible actualizar el usuario.");
        }
    }

    @Override
    public void deleteOne(String id) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible eliminar el usuario.");
        }
    }

    private UsuarioDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new UsuarioDTO.UsuarioBuilder()
                .setUsuarioId(resultSet.getInt("id_usuario"))
                .setNombreUsuario(resultSet.getString("username"))
                .setContrasenia(resultSet.getString("password"))
                .setRol(RolUsuario.valueOf(resultSet.getString("role")))
                .setAcceso(resultSet.getBoolean("access"))
                .build();
    }
}
