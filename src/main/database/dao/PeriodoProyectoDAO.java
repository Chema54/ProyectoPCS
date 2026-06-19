package main.database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import main.common.ExceptionHandler;
import main.common.UserDisplayableException;
import main.database.DBConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PeriodoProyectoDAO {

    private static final Logger LOGGER = LogManager.getLogger(PeriodoProyectoDAO.class);

    private static final String INSERT_QUERY =
            "INSERT INTO Periodo_Proyecto (id_periodo, id_proyecto) VALUES (?, ?)";

    public void linkProjectsToPeriod(int periodId, List<Integer> projectIds) throws UserDisplayableException {
        if (projectIds == null || projectIds.isEmpty()) return;
        
        try (
            Connection connection = DBConnector.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)
        ) {
            for (int projectId : projectIds) {
                statement.setInt(1, periodId);
                statement.setInt(2, projectId);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw ExceptionHandler.handleSQLException(
                    LOGGER, e, "No se han podido vincular los proyectos al periodo, debido a un error de conexión con la Base de datos");
        }
    }
}
