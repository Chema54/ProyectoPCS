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

    // Ajustado para omitir id_usuario (ya que es autoincrementable) y usar id_rol
    private static final String CREATE_QUERY =
            "INSERT INTO Usuario (username, password, role, access, id_rol) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Usuario";

    private static final String GET_QUERY =
            "SELECT * FROM Usuario WHERE username = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Usuario SET username = ?, password = ?, role = ?, access = ? WHERE id_usuario = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Usuario WHERE id_usuario = ?";

    @Override
    public void createOne(UsuarioDTO userDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getPassword());
            statement.setString(3, userDTO.getRole().name());
            statement.setBoolean(4, userDTO.hasAccess());
            statement.setInt(5, userDTO.getRole().getIdRol());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No ha sido posible crear el usuario.");
        }
    }

    public int createOneAndReturnId(UsuarioDTO userDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getPassword());
            statement.setString(3, userDTO.getRole().name());
            statement.setBoolean(4, userDTO.hasAccess());
            statement.setInt(5, userDTO.getRole().getIdRol());

            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int newId = generatedKeys.getInt(1);
                    
                    // Native MySQL User creation
                    try (java.sql.Statement nativeStmt = connection.createStatement()) {
                        String username = userDTO.getNombreUsuario();
                        String password = userDTO.getPassword();
                        nativeStmt.execute("CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password + "'");
                        nativeStmt.execute("GRANT ALL PRIVILEGES ON practicas_profesionales.* TO '" + username + "'@'%'");
                        nativeStmt.execute("FLUSH PRIVILEGES");
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
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
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
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_QUERY)
        ) {
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
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, userDTO.getNombreUsuario());
            statement.setString(2, userDTO.getPassword());
            statement.setString(3, userDTO.getRole().name());
            statement.setBoolean(4, userDTO.hasAccess());
            statement.setInt(5, userDTO.getUserID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible actualizar el usuario.");
        }
    }

    @Override
    public void deleteOne(String id) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible eliminar el usuario.");
        }
    }

    private UsuarioDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new UsuarioDTO.UsuarioBuilder()
            .setUserID(resultSet.getInt("id_usuario"))
            .setNombreUsuario(resultSet.getString("username"))
            .setPassword(resultSet.getString("password"))
            .setRole(RolUsuario.valueOf(resultSet.getString("role")))
            .setAccess(resultSet.getBoolean("access"))
            .build();
    }
}
