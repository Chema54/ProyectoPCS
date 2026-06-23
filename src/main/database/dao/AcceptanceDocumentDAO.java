package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.database.dao.shape.CompleteDAOShape;
import main.business.dto.AcceptanceDocumentDTO;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AcceptanceDocumentDAO extends CompleteDAOShape<AcceptanceDocumentDTO, Integer> {

    private static final Logger LOGGER = LogManager.getLogger(AcceptanceDocumentDAO.class);
    

    private static final String CREATE_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, archivo, estado) VALUES (?, ?, ?, ?)";

    private static final String GET_ALL_QUERY =
            "SELECT * FROM Documento_Aceptacion";

    private static final String GET_QUERY =
            "SELECT * FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";

    private static final String UPDATE_QUERY =
            "UPDATE Documento_Aceptacion SET id_asignacion = ?, nombre_entregable = ?, archivo = ?, estado = ? WHERE id_doc_aceptacion = ?";

    private static final String DELETE_QUERY =
            "DELETE FROM Documento_Aceptacion WHERE id_doc_aceptacion = ?";
            
    private static final String BATCH_INSERT_QUERY =
            "INSERT INTO Documento_Aceptacion (id_asignacion, nombre_entregable, estado) VALUES (?, ?, 'Inhabilitado')";

    @Override
    public void createOne(AcceptanceDocumentDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getDeliverableName());
            statement.setBytes(3, documentDTO.getFile());
            statement.setString(4, documentDTO.getStatus() != null ? documentDTO.getStatus() : "Pendiente");

            statement.executeUpdate();

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    public void createDeliverables(List<Integer> assignmentIds) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(BATCH_INSERT_QUERY)
        ) {
            for (Integer assignmentId : assignmentIds) {
                statement.setInt(1, assignmentId);
                statement.setString(2, "Oficio de aceptacion");
                statement.executeUpdate();
                
                statement.setInt(1, assignmentId);
                statement.setString(2, "Cronograma de actividades");
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public List<AcceptanceDocumentDTO> getAll() throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_QUERY);
            ResultSet resultSet = statement.executeQuery()
        ) {
            List<AcceptanceDocumentDTO> documents = new ArrayList<>();

            while (resultSet.next()) {
                documents.add(mapResultSetToDTO(resultSet));
            }

            return documents;

        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public AcceptanceDocumentDTO getOne(Integer id) throws UserDisplayableException {
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }

    @Override
    public void updateOne(AcceptanceDocumentDTO documentDTO) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            statement.setInt(1, documentDTO.getAssignmentId());
            statement.setString(2, documentDTO.getDeliverableName());
            statement.setBytes(3, documentDTO.getFile());
            statement.setString(4, documentDTO.getStatus());
            statement.setInt(5, documentDTO.getAcceptanceDocumentId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
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
            throw ExceptionHandler.handleSQLException(LOGGER, e, "No se ha podido realizar la operación, debido a un error de conexión.");
        }
    }
    
    public List<AcceptanceDocumentDTO> getAllByAssignmentId(int assignmentId) throws UserDisplayableException {
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Documento_Aceptacion WHERE id_asignacion = ?")
        ) {
            statement.setInt(1, assignmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<AcceptanceDocumentDTO> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapResultSetToDTO(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(LOGGER, e, "Error consultando los documentos.");
        }
    }

    private AcceptanceDocumentDTO mapResultSetToDTO(ResultSet resultSet) throws SQLException {
        return new AcceptanceDocumentDTO.AcceptanceDocumentBuilder()
            .setAcceptanceDocumentId(resultSet.getInt("id_doc_aceptacion"))
            .setAssignmentId(resultSet.getInt("id_asignacion"))
            .setDeliverableName(resultSet.getString("nombre_entregable"))
            .setFile(resultSet.getBytes("archivo"))
            .setStatus(resultSet.getString("estado"))
            .build();
    }
}
