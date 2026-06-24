package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.PeriodoDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PeriodoDAO extends MoldeDAOCompleto<PeriodoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(PeriodoDAO.class);
    
    

    private static final String CREATE_QUERY =
            "INSERT INTO Periodo (nombre, fecha_inicio, fecha_fin) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Periodo";

    private static final String GET_QUERY =
            "SELECT * FROM Periodo WHERE id_periodo = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Periodo SET nombre = ?, fecha_inicio = ?, fecha_fin = ? WHERE id_periodo = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Periodo WHERE id_periodo = ?";

    @Override
    public void createOne(PeriodoDTO periodDTO) throws ExcepcionMostrableUsuario {
        createAndReturnId(periodDTO);
    }

    public Integer createAndReturnId(PeriodoDTO periodDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, periodDTO.getNombre());
            statement.setDate(2, periodDTO.getFechaInicio());
            statement.setDate(3, periodDTO.getFechaFin());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Error al crear el periodo, no se pudo obtener el ID.");
                }
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<PeriodoDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<PeriodoDTO> periods = new ArrayList<>();

            while (resultSet.next()) {
                periods.add(mapResultSetToDTO(resultSet));
            }

            return periods;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public PeriodoDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
    public void updateOne(PeriodoDTO periodDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, periodDTO.getNombre());
            statement.setDate(2, periodDTO.getFechaInicio());
            statement.setDate(3, periodDTO.getFechaFin());
            statement.setInt(4, periodDTO.getPeriodoId());
            
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

    private PeriodoDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new PeriodoDTO.PeriodBuilder()
            .setPeriodoId(resultSet.getInt("id_periodo"))
            .setNombre(resultSet.getString("nombre"))
            .setFechaInicio(resultSet.getDate("fecha_inicio"))
            .setFechaFin(resultSet.getDate("fecha_fin"))
            .build();
    }
}
