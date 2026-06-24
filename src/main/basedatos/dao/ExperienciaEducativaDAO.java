package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExperienciaEducativaDAO extends MoldeDAOCompleto<ExperienciaEducativaDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ExperienciaEducativaDAO.class);
    
    

    private static final String CREATE_QUERY =
            "INSERT INTO ExperienciaEducativa (nombre, id_periodo, nrc) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT e.*, p.nombre as periodName, CONCAT_WS(' ', prof.nombre, prof.apellido_paterno, prof.apellido_materno) as professorName FROM ExperienciaEducativa e INNER JOIN Periodo p ON e.id_periodo = p.id_periodo LEFT JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia LEFT JOIN Profesor prof ON pe.id_profesor = prof.id_profesor";

    private static final String GET_QUERY =
            "SELECT e.*, p.nombre as periodName, CONCAT_WS(' ', prof.nombre, prof.apellido_paterno, prof.apellido_materno) as professorName FROM ExperienciaEducativa e INNER JOIN Periodo p ON e.id_periodo = p.id_periodo LEFT JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia LEFT JOIN Profesor prof ON pe.id_profesor = prof.id_profesor WHERE e.id_experiencia = ?";

    private static final String UPDATE_QUERY =
            "UPDATE ExperienciaEducativa SET nombre = ?, id_periodo = ?, nrc = ? WHERE id_experiencia = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM ExperienciaEducativa WHERE id_experiencia = ?";

    @Override
    public void createOne(ExperienciaEducativaDTO experienceDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, experienceDTO.getNombre());
            
            if (experienceDTO.getPeriodoId() != null) {
                statement.setInt(2, experienceDTO.getPeriodoId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            
            statement.setString(3, experienceDTO.getNrc());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<ExperienciaEducativaDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ExperienciaEducativaDTO> experiences = new ArrayList<>();

            while (resultSet.next()) {
                experiences.add(mapResultSetToDTO(resultSet));
            }

            return experiences;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public ExperienciaEducativaDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(ExperienciaEducativaDTO experienceDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, experienceDTO.getNombre());
            
            if (experienceDTO.getPeriodoId() != null) {
                statement.setInt(2, experienceDTO.getPeriodoId());
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            
            statement.setString(3, experienceDTO.getNrc());
            statement.setInt(4, experienceDTO.getExperienciaEducativaId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }
    
        public List<ExperienciaEducativaDTO> getExperienciasByUserId(int userId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT e.*, p.nombre as periodName, concat(prof.nombre, ' ', prof.apellido_paterno) as professorName FROM ExperienciaEducativa e INNER JOIN ProfesorExperiencia pe ON e.id_experiencia = pe.id_experiencia INNER JOIN Profesor prof ON pe.id_profesor = prof.id_profesor LEFT JOIN Periodo p ON e.id_periodo = p.id_periodo WHERE prof.id_usuario = ?")
        ) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<ExperienciaEducativaDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error al buscar las experiencias educativas del profesor.");
        }
    }

private ExperienciaEducativaDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        int periodId = resultSet.getInt("id_periodo");
        Integer periodIdOrNull = resultSet.wasNull() ? null : periodId;
        
        return new ExperienciaEducativaDTO.EducationalExperienceBuilder()
            .setExperienciaEducativaId(resultSet.getInt("id_experiencia"))
            .setNombre(resultSet.getString("nombre"))
            .setPeriodoId(periodIdOrNull)
            .setNrc(resultSet.getString("nrc"))
            .setNombrePeriodo(resultSet.getString("periodName"))
            .setNombreProfesor(resultSet.getString("professorName") != null ? resultSet.getString("professorName").trim() : "Sin asignar")
            .build();
    }
}
