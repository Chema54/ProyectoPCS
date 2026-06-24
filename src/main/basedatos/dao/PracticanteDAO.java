package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.PracticanteDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PracticanteDAO extends MoldeDAOCompleto<PracticanteDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(PracticanteDAO.class);

    private static final String CREATE_INTERN_QUERY =
            "INSERT INTO Practicante (nombre, apellido_paterno, apellido_materno, correo, matricula, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT p.*, u.username FROM Practicante p LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario";

    private static final String GET_QUERY =
            "SELECT p.*, u.username FROM Practicante p LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario WHERE p.id_practicante = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Practicante SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ?, matricula = ?, estado = ? WHERE id_practicante = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Practicante WHERE id_practicante = ?";

    private static final String CHANGE_STATUS_QUERY =
            "UPDATE Practicante SET estado = ? WHERE id_practicante = ?";

    private static final String CHECK_ENROLLMENT_QUERY =
            "SELECT COUNT(*) FROM Practicante WHERE matricula = ?";

    @Override
    public void createOne(PracticanteDTO internDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement internStatement = connection.prepareStatement(CREATE_INTERN_QUERY)
        ) {
            internStatement.setString(1, internDTO.getNombre());
            internStatement.setString(2, internDTO.getApellidoPaterno());
            internStatement.setString(3, internDTO.getApellidoMaterno());
            internStatement.setString(4, internDTO.getCorreo());
            internStatement.setString(5, internDTO.getMatricula());
            internStatement.setString(6, internDTO.getEstado() != null ? internDTO.getEstado() : "Activo");
            internStatement.setInt(7, internDTO.getUsuarioId());
            
            internStatement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    public int createOneAndReturnId(PracticanteDTO internDTO) throws ExcepcionMostrableUsuario {
        int generatedId = -1;
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement internStatement = connection.prepareStatement(CREATE_INTERN_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            internStatement.setString(1, internDTO.getNombre());
            internStatement.setString(2, internDTO.getApellidoPaterno());
            internStatement.setString(3, internDTO.getApellidoMaterno());
            internStatement.setString(4, internDTO.getCorreo());
            internStatement.setString(5, internDTO.getMatricula());
            internStatement.setString(6, internDTO.getEstado() != null ? internDTO.getEstado() : "Activo");
            internStatement.setInt(7, internDTO.getUsuarioId());
            
            internStatement.executeUpdate();
            
            try (ResultSet generatedKeys = internStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del practicante generado.");
                }
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido realizar el registro del practicante, debido a un error de conexión con la Base de datos");
        }
        return generatedId;
    }

    @Override
    public List<PracticanteDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<PracticanteDTO> interns = new ArrayList<>();

            while (resultSet.next()) {
                interns.add(mapResultSetToDTO(resultSet));
            }

            return interns;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de practicantes, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public PracticanteDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
                    LOGGER, e, "No se ha podido realizar la búsqueda del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(PracticanteDTO internDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, internDTO.getNombre());
            statement.setString(2, internDTO.getApellidoPaterno());
            statement.setString(3, internDTO.getApellidoMaterno());
            statement.setString(4, internDTO.getCorreo());
            statement.setString(5, internDTO.getMatricula());
            statement.setString(6, internDTO.getEstado());
            statement.setInt(7, internDTO.getPracticanteId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del practicante, debido a un error de conexión con la Base de datos");
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
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la eliminación del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    public void changeStatus(int internId, String status) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHANGE_STATUS_QUERY)
        ) {
            statement.setString(1, status);
            statement.setInt(2, internId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido actualizar el estado del practicante, debido a un error de conexión con la Base de datos");
        }
    }

    public boolean isEnrollmentRegistered(String enrollment) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CHECK_ENROLLMENT_QUERY)
        ) {
            statement.setString(1, enrollment);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER, e, "No se ha podido verificar la matrícula, debido a un error de conexión con la Base de datos");
        }
        return false;
    }

    public PracticanteDTO getByEnrollment(String enrollment) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT p.*, u.username FROM Practicante p LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario WHERE p.matricula = ?")
        ) {
            statement.setString(1, enrollment);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error obteniendo el practicante.");
        }
        return null;
    }

    private PracticanteDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new PracticanteDTO.InternBuilder()
            .setPracticanteId(resultSet.getInt("id_practicante"))
            .setNombre(resultSet.getString("nombre"))
            .setApellidoPaterno(resultSet.getString("apellido_paterno"))
            .setApellidoMaterno(resultSet.getString("apellido_materno"))
            .setCorreo(resultSet.getString("correo"))
            .setMatricula(resultSet.getString("matricula"))
            .setEstado(resultSet.getString("estado"))
            .setUsuarioId(resultSet.getInt("id_usuario"))
            .setNombreUsuario(resultSet.getString("username"))
            .build();
    }
}
