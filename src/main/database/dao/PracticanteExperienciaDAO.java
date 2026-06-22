package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.PracticanteExperienciaDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PracticanteExperienciaDAO extends CompleteDAOShape<PracticanteExperienciaDTO, String> {

    private static final Logger LOGGER = LogManager.getLogger(PracticanteExperienciaDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO PracticanteExperiencia (id_practicante, id_experiencia) VALUES (?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM PracticanteExperiencia";

    private static final String DELETE_QUERY =
            "DELETE FROM PracticanteExperiencia WHERE id_practicante = ? AND id_experiencia = ?";

    @Override
    public void createOne(PracticanteExperienciaDTO dto) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, dto.getInternId());
            statement.setInt(2, dto.getExperienceId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible registrar la relación de practicante y experiencia.");
        }
    }

    @Override
    public List<PracticanteExperienciaDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<PracticanteExperienciaDTO> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(mapResultSetToDTO(resultSet));
            }
            return list;
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible cargar las relaciones de practicante y experiencia.");
        }
    }

    @Override
    public PracticanteExperienciaDTO getOne(String idCompuesto) throws UserDisplayableException {
        throw new UnsupportedOperationException("Método getOne no soportado para llaves compuestas en este DAO.");
    }

    @Override
    public void updateOne(PracticanteExperienciaDTO dto) throws UserDisplayableException {
        throw new UnsupportedOperationException("Método updateOne no soportado para llaves compuestas en este DAO.");
    }

    @Override
    public void deleteOne(String idCompuesto) throws UserDisplayableException {
        String[] ids = idCompuesto.split("-");
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setInt(1, Integer.parseInt(ids[0]));
            statement.setInt(2, Integer.parseInt(ids[1]));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No ha sido posible eliminar la relación de practicante y experiencia.");
        }
    }

    private PracticanteExperienciaDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new PracticanteExperienciaDTO.PracticanteExperienciaBuilder()
            .setInternId(resultSet.getInt("id_practicante"))
            .setExperienceId(resultSet.getInt("id_experiencia"))
            .build();
    }
}
