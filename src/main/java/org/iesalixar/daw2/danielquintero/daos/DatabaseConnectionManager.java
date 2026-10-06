package org.iesalixar.daw2.danielquintero.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase encargada de gestionar la conexión con la base de datos.
 * Implementa el patrón Singleton para mantener una única instancia.
 */
public class DatabaseConnectionManager {

    private static final Logger logger =
            LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private static DatabaseConnectionManager instance;

    private Connection connection;

    /**
     * Constructor privado que carga la configuración de la base de datos
     * y establece la conexión.
     */
    private DatabaseConnectionManager() {
        logger.info("Entrando en el constructor de DatabaseConnectionManager");

        Properties properties = new Properties();

        try (InputStream inputStream =
                     getClass().getClassLoader().getResourceAsStream("application.properties")) {

            if (inputStream == null) {
                logger.error("No se ha encontrado application.properties");
                throw new RuntimeException("No se ha encontrado application.properties");
            }

            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            logger.info("Intentando conectar con la base de datos");

            connection = DriverManager.getConnection(url, user, password);

            logger.info("Conexión con la base de datos establecida correctamente");

        } catch (IOException e) {
            logger.error("Error al leer application.properties", e);
            throw new RuntimeException("Error al leer application.properties", e);

        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos", e);
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }

        logger.info("Saliendo del constructor de DatabaseConnectionManager");
    }

    /**
     * Devuelve la única instancia de DatabaseConnectionManager.
     *
     * @return instancia única de DatabaseConnectionManager
     */
    public static DatabaseConnectionManager getInstance() {
        logger.info("Entrando en getInstance");

        if (instance == null) {
            instance = new DatabaseConnectionManager();
        }

        logger.info("Saliendo de getInstance");
        return instance;
    }

    /**
     * Devuelve la conexión actual a la base de datos.
     *
     * @return conexión JDBC
     */
    public Connection getConnection() {
        logger.info("Entrando en getConnection");
        logger.info("Saliendo de getConnection");

        return connection;
    }
}