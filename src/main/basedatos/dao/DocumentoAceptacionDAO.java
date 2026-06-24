package main.basedatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.basedatos.dao.shape.MoldeDAOCompleto;
import main.negocio.dto.DocumentoAceptacionDTO;
import main.comun.ManejadorExcepciones;
import main.comun.ExcepcionMostrableUsuario;
import main.basedatos.ConexionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentoAceptacionDAO extends MoldeDAOCompleto<DocumentoAceptacionDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(DocumentoAceptacionDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, archivo, estado, fecha_limite) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Documento_Aceptacion";

    private static final String GET_QUERY =
            "SELECT * FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Documento_Aceptacion SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_limite = ? WHERE id_doc_aceptacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(DocumentoAceptacionDTO documentDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAsignacionId());
            statement.setString(2, documentDTO.getNombreEntregable());
            statement.setBytes(3, documentDTO.getArchivo());
            statement.setString(4, documentDTO.getEstado() != null ? documentDTO.getEstado() : "Pendiente");
            statement.setDate(5, documentDTO.getFechaLimite());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void createEntregables(List<Integer> assignmentIds) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)
        ) {
            for (Integer asignacionId : assignmentIds) {
                statement.setInt(1, asignacionId);
                statement.setString(2, "Oficio de aceptacion");
                statement.executeUpdate();
                
                statement.setInt(1, asignacionId);
                statement.setString(2, "Cronograma de actividades");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<DocumentoAceptacionDTO> getAll() throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<DocumentoAceptacionDTO> documents = new ArrayList<>();

            while (resultSet.next()) {
                documents.add(mapResultSetToDTO(resultSet));
            }

            return documents;

        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public DocumentoAceptacionDTO getOne(Integer id) throws ExcepcionMostrableUsuario {
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
    public void updateOne(DocumentoAceptacionDTO documentDTO) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAsignacionId());
            statement.setString(2, documentDTO.getNombreEntregable());
            statement.setBytes(3, documentDTO.getArchivo());
            statement.setString(4, documentDTO.getEstado());
            statement.setDate(5, documentDTO.getFechaLimite());
            statement.setInt(6, documentDTO.getDocumentoAceptacionId());
            
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
    
    public List<DocumentoAceptacionDTO> getAllByAssignmentId(int asignacionId) throws ExcepcionMostrableUsuario {
        try (
            Connection connection = ConexionBD.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Documento_Aceptacion WHERE id_asignacion = ?")
        ) {
            statement.setInt(1, asignacionId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<DocumentoAceptacionDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ManejadorExcepciones.handleSQLException(LOGGER, e, "Error consultando los documentos.");
        }
    }

    private DocumentoAceptacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new DocumentoAceptacionDTO.AcceptanceDocumentBuilder()
            .setDocumentoAceptacionId(resultSet.getInt("id_doc_aceptacion"))
            .setAsignacionId(resultSet.getInt("id_asignacion"))
            .setNombreEntregable(resultSet.getString("nombre_entregable"))
            .setArchivo(resultSet.getBytes("archivo"))
            .setEstado(resultSet.getString("estado"))
            .setFechaLimite(resultSet.getDate("fecha_limite"))
            .build();
    }
}
