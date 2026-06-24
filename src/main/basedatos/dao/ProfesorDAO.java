package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.ConexionBD;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.ManejadorExcepciones;
import main.negocio.dto.ProfesorDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProfesorDAO extends MoldeDAOCompleto<ProfesorDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ProfesorDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO Profesor (numero_personal, nombre, apellido_paterno, apellido_materno, correo, estado, id_usuario) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT p.*, u.username "
            + "FROM Profesor p "
            + "LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario";

    private static final String GET_QUERY =
            "SELECT p.*, u.username "
            + "FROM Profesor p "
            + "LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario "
            + "WHERE p.id_profesor = ?";

    private static final String GET_BY_EXPERIENCE_QUERY =
            "SELECT p.*, u.username "
            + "FROM Profesor p "
            + "LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario "
            + "INNER JOIN ProfesorExperiencia pe ON p.id_profesor = pe.id_profesor "
            + "WHERE pe.id_experiencia = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Profesor "
            + "SET numero_personal = ?, nombre = ?, apellido_paterno = ?, apellido_materno = ?, correo = ?, estado = ? "
            + "WHERE id_profesor = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Profesor WHERE id_profesor = ?";

    @Override
    public void createOne(ProfesorDTO professorDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, professorDTO.getNumeroPersonal());
            statement.setString(2, professorDTO.getNombre());
            statement.setString(3, professorDTO.getApellidoPaterno());
            statement.setString(4, professorDTO.getApellidoMaterno());
            statement.setString(5, professorDTO.getCorreo());
            statement.setString(6, professorDTO.getEstado() != null ? professorDTO.getEstado() : "Activo");
            statement.setInt(7, professorDTO.getUsuarioId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido realizar el registro del profesor, debido a un error de conexión con la Base de datos."
            );
        }
    }

    @Override
    public List<ProfesorDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ProfesorDTO> professors = new ArrayList<>();

            while (resultSet.next()) {
                professors.add(mapResultSetToDTO(resultSet));
            }

            return professors;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido realizar la consulta de profesores, debido a un error de conexión con la Base de datos."
            );
        }
    }

    @Override
    public ProfesorDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
                    LOGGER,
                    e,
                    "No se ha podido realizar la búsqueda del profesor, debido a un error de conexión con la Base de datos."
            );
        }
    }

    @Override
    public void updateOne(ProfesorDTO professorDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, professorDTO.getNumeroPersonal());
            statement.setString(2, professorDTO.getNombre());
            statement.setString(3, professorDTO.getApellidoPaterno());
            statement.setString(4, professorDTO.getApellidoMaterno());
            statement.setString(5, professorDTO.getCorreo());
            statement.setString(6, professorDTO.getEstado());
            statement.setInt(7, professorDTO.getProfesorId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido realizar la actualización del profesor, debido a un error de conexión con la Base de datos."
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
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido realizar la eliminación del profesor, debido a un error de conexión con la Base de datos."
            );
        }
    }

    public ProfesorDTO getProfessorForExperience(int experienceId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_BY_EXPERIENCE_QUERY)
        ) {
            statement.setInt(1, experienceId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }

                return null;
            }

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido obtener el profesor para la experiencia."
            );
        }
    }

    public List<ProfesorDTO> getAvailableProfessors() throws ExcepcionMostrableUsuario {
        String query =
                "SELECT p.*, u.username "
                + "FROM Profesor p "
                + "LEFT JOIN Usuario u ON p.id_usuario = u.id_usuario "
                + "WHERE p.estado = 'Activo' "
                + "ORDER BY p.apellido_paterno, p.apellido_materno, p.nombre";

        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ProfesorDTO> professors = new ArrayList<>();

            while (resultSet.next()) {
                professors.add(mapResultSetToDTO(resultSet));
            }

            return professors;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(
                    LOGGER,
                    e,
                    "No se ha podido consultar los profesores disponibles."
            );
        }
    }

    private ProfesorDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ProfesorDTO.ProfesorBuilder()
                .setProfesorId(resultSet.getInt("id_profesor"))
                .setNumeroPersonal(resultSet.getString("numero_personal"))
                .setNombre(resultSet.getString("nombre"))
                .setApellidoPaterno(resultSet.getString("apellido_paterno"))
                .setApellidoMaterno(resultSet.getString("apellido_materno"))
                .setCorreo(resultSet.getString("correo"))
                .setEstado(resultSet.getString("estado"))
                .setUsuarioId(resultSet.getInt("id_usuario"))
                .setNombreUsuario(resultSet.getString("username"))
                .build();
    }
}