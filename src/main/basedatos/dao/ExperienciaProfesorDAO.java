package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.ExperienciaProfesorDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExperienciaProfesorDAO extends MoldeDAOCompleto<ExperienciaProfesorDTO, String> {

    private static final Logger LOGGER = LogManager.getLogger(ExperienciaProfesorDAO.class);

    private static final String CREATE_QUERY
            = "INSERT INTO ProfesorExperiencia (id_profesor, id_experiencia) VALUES (?, ?)";

    private static final String GET_ALL_QUERY
            = "SELECT * FROM ProfesorExperiencia";

    private static final String DELETE_QUERY
            = "DELETE FROM ProfesorExperiencia WHERE id_profesor = ? AND id_experiencia = ?";

    @Override
    public void createOne(ExperienciaProfesorDTO dto) throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, dto.getProfesorId());
            statement.setInt(2, dto.getExperienciaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible registrar la relación de profesor y experiencia.");
        }
    }

    @Override
    public List<ExperienciaProfesorDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY); ResultSet resultSet = statement.executeQuery()) {
            List<ExperienciaProfesorDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapResultSetToDTO(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible cargar las relaciones de profesor y experiencia.");
        }
    }

    @Override
    public ExperienciaProfesorDTO getOne(String idCompuesto) throws ExcepcionMostrableUsuario {
        throw new UnsupportedOperationException("Método getOne no soportado para llaves compuestas en este DAO.");
    }

    @Override
    public void updateOne(ExperienciaProfesorDTO dto) throws ExcepcionMostrableUsuario {
        throw new UnsupportedOperationException("Método updateOne no soportado para llaves compuestas en este DAO.");
    }

    @Override
    public void deleteOne(String idCompuesto) throws ExcepcionMostrableUsuario {
        String[] ids = idCompuesto.split("-");
        try (
                Connection connection = ConexionBD.getInstance().getConnection(); PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, Integer.parseInt(ids[0]));
            statement.setInt(2, Integer.parseInt(ids[1]));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No ha sido posible eliminar la relación de profesor y experiencia.");
        }
    }

    private ExperienciaProfesorDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ExperienciaProfesorDTO.ExperienciaProfesorBuilder()
                .setProfesorId(resultSet.getInt("id_profesor"))
                .setExperienciaId(resultSet.getInt("id_experiencia"))
                .build();
    }
}
