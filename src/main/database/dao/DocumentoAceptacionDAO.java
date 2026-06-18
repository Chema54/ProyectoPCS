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

    private static final String CREATE_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, archivo, estado, fecha_entrega) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Documento_Aceptacion";

    private static final String GET_QUERY =
            "SELECT * FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Documento_Aceptacion SET id_asignacion = ?, archivo = ?, estado = ?, fecha_entrega = ? WHERE id_doc_aceptacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";
            
    private static final String CREATE_SHELL_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, estado) VALUES (?, 'Inhabilitado')";

    @Override
    public void createOne(DocumentoAceptacionDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getFile());
            statement.setString(3, documentDTO.getStatus() != null ? documentDTO.getStatus() : "Pendiente");
            statement.setDate(4, documentDTO.getDeliveryDate());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e);
        }
    }

    public static void crearCascaron(int assignmentId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_SHELL_QUERY)
        ) {
            statement.setInt(1, assignmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la generación del cascarón de aceptacion, debido a un error de conexión con la Base de datos");
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
                documents.add(new DocumentoAceptacionDTO.DocumentoAceptacionBuilder()
                    .setAcceptanceDocumentId(resultSet.getInt("id_doc_aceptacion"))
                    .setAssignmentId(resultSet.getInt("id_asignacion"))
                    .setFile(resultSet.getString("archivo"))
                    .setStatus(resultSet.getString("estado"))
                    .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                    .build()
                );
            }

            return documents;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la consulta de documentos de aceptacion, debido a un error de conexión con la Base de datos");
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
                    return new DocumentoAceptacionDTO.DocumentoAceptacionBuilder()
                        .setAcceptanceDocumentId(resultSet.getInt("id_doc_aceptacion"))
                        .setAssignmentId(resultSet.getInt("id_asignacion"))
                        .setFile(resultSet.getString("archivo"))
                        .setStatus(resultSet.getString("estado"))
                        .setDeliveryDate(resultSet.getDate("fecha_entrega"))
                        .build();
                }
                return null;
            }

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la búsqueda del documento de aceptacion, debido a un error de conexión con la Base de datos");
        }
    }

    @Override
    public void updateOne(DocumentoAceptacionDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getFile());
            statement.setString(3, documentDTO.getStatus());
            statement.setDate(4, documentDTO.getDeliveryDate());
            statement.setInt(5, documentDTO.getAcceptanceDocumentId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la actualización del documento de aceptacion, debido a un error de conexión con la Base de datos");
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
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se ha podido realizar la eliminación del documento de aceptacion, debido a un error de conexión con la Base de datos");
        }
    }
}
