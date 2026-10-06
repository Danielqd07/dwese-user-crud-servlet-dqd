package org.iesalixar.daw2.danielquintero.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Clase encargada de cargar y ejecutar el contenido de un archivo SQL.
 */
public class DataInitializer {

    private static final Logger logger =
            LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Ejecuta las sentencias SQL contenidas en el InputStream recibido.
     *
     * @param sqlFileStream flujo de entrada del archivo SQL
     * @throws SQLException si ocurre un error al ejecutar SQL
     * @throws IOException  si ocurre un error al leer el archivo
     */
    public static void loadDataFromSQL(InputStream sqlFileStream)
            throws SQLException, IOException {

        logger.info("Entrando en loadDataFromSQL");

        if (sqlFileStream == null) {
            logger.error("El archivo SQL es nulo");
            throw new IOException("No se ha encontrado el archivo SQL");
        }

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        String sql;

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(sqlFileStream))) {

            sql = reader.lines()
                    .collect(Collectors.joining("\n"));
        }

        String[] statements = sql.split(";");

        connection.setAutoCommit(false);

        try {

            try (Statement statement = connection.createStatement()) {

                for (String sqlStatement : statements) {

                    if (!sqlStatement.trim().isEmpty()) {

                        logger.info("Ejecutando sentencia SQL");

                        statement.execute(sqlStatement.trim());
                    }
                }
            }

            connection.commit();

            logger.info("Archivo SQL ejecutado correctamente");

        } catch (SQLException e) {

            connection.rollback();

            logger.error("Error al ejecutar el archivo SQL", e);

            throw e;

        } finally {

            connection.setAutoCommit(true);
        }

        logger.info("Saliendo de loadDataFromSQL");
    }
}