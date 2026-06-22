package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.DocumentoAceptacionDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DocumentoAceptacionDAO extends CompleteDAOShape<DocumentoAceptacionDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(DocumentoAceptacionDAO.class);
    private static final String MSG_SQL_EXCEPTION = "No se ha podido realizar La operación, debido a un error de conexión con la Base de datos";

    private static final String CREATE_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, archivo, estado, fecha_entrega) VALUES (?, ?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Documento_Aceptacion";

    private static final String GET_QUERY =
            "SELECT * FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Documento_Aceptacion SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ?, fecha_entrega = ? WHERE id_doc_aceptacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, estado) VALUES (?, 'Documentos iniciales', 'Inhabilitado')";

    @Override
    public void createOne(DocumentoAceptacionDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getDeliverableName());
            statement.setString(3, documentDTO.getFile());
            statement.setString(4, documentDTO.getStatus() != null ? documentDTO.getStatus() : "Pendiente");
            statement.setDate(5, documentDTO.getDeliveryDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }

    public void createDeliverables(List<Integer> assignmentIds) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)
        ) {
            for (Integer assignmentId : assignmentIds) {
                statement.setInt(1, assignmentId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }

    @Override
    public List<DocumentoAceptacionDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<DocumentoAceptacionDTO> documents = new ArrayList<>();

            while (resultSet.next()) {
                documents.add(mapResultSetToDTO(resultSet));
            }

            return documents;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }

    @Override
    public DocumentoAceptacionDTO getOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }

    @Override
    public void updateOne(DocumentoAceptacionDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getDeliverableName());
            statement.setString(3, documentDTO.getFile());
            statement.setString(4, documentDTO.getStatus());
            statement.setDate(5, documentDTO.getDeliveryDate());
            statement.setInt(6, documentDTO.getAcceptanceDocumentId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }

    @Override
    public void deleteOne(Integer id) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, MSG_SQL_EXCEPTION);
        }
    }
    
    private DocumentoAceptacionDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new DocumentoAceptacionDTO.DocumentoAceptacionBuilder()
            .setAcceptanceDocumentId(resultSet.getInt("id_doc_aceptacion"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getString("archivo"))
            .setStatus(resultSet.getString("estado"))
            .setDeliveryDate(resultSet.getDate("fecha_entrega"))
            .build();
    }
}
