package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.ResponsableProyectoDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponsableProyectoDAO extends MoldeDAOCompleto<ResponsableProyectoDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ResponsableProyectoDAO.class);

    private static final String CREATE_QUERY =
            "INSERT INTO TitularProyecto (nombre, numero_personal, id_organizacion) VALUES (?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM TitularProyecto";

    private static final String GET_QUERY =
            "SELECT * FROM TitularProyecto WHERE id_titular = ?";

    private static final String GET_BY_NUMERO_QUERY =
            "SELECT * FROM TitularProyecto WHERE numero_personal = ?";

    private static final String UPDATE_QUERY =
            "UPDATE TitularProyecto SET nombre = ?, numero_personal = ?, id_organizacion = ? WHERE id_titular = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM TitularProyecto WHERE id_titular = ?";

    @Override
    public void createOne(ResponsableProyectoDTO titularDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setString(1, titularDTO.getNombre());
            statement.setString(2, titularDTO.getNumeroPersonal());
            statement.setInt(3, titularDTO.getOrganizacionId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar el registro del titular, debido a un error de conexión con la Base de datos");
        }
    }

    public ResponsableProyectoDTO getByNumeroPersonal(String numeroPersonal) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_BY_NUMERO_QUERY)
        ) {
            statement.setString(1, numeroPersonal);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDTO(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido buscar al titular por su número personal.");
        }
    }

    @Override
    public List<ResponsableProyectoDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<ResponsableProyectoDTO> titulares = new ArrayList<>();

            while (resultSet.next()) {
                titulares.add(mapResultSetToDTO(resultSet));
            }

            return titulares;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la consulta de titulares, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public ResponsableProyectoDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la búsqueda del titular, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(ResponsableProyectoDTO titularDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setString(1, titularDTO.getNombre());
            statement.setString(2, titularDTO.getNumeroPersonal());
            statement.setInt(3, titularDTO.getOrganizacionId());
            statement.setInt(4, titularDTO.getTitularId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la actualización del titular, debido a un error de conexión con la Base de datos");
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
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la eliminación del titular, debido a un error de conexión con la Base de datos");
        }
    }
    
    private ResponsableProyectoDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new ResponsableProyectoDTO.TitularBuilder()
            .setTitularId(resultSet.getInt("id_titular"))
            .setNombre(resultSet.getString("nombre"))
            .setNumeroPersonal(resultSet.getString("numero_personal"))
            .setOrganizacionId(resultSet.getInt("id_organizacion"))
            .build();
    }
}
