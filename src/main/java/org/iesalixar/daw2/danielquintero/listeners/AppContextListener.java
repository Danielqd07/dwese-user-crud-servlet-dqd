package org.iesalixar.daw2.danielquintero.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.iesalixar.daw2.danielquintero.daos.DataInitializer;
import org.iesalixar.daw2.danielquintero.daos.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Listener encargado de inicializar la aplicación
 * y cargar los datos iniciales de la base de datos.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger =
            LoggerFactory.getLogger(AppContextListener.class);

    /**
     * Método ejecutado al iniciar la aplicación.
     *
     * @param sce evento del contexto de la aplicación
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        logger.info("Entrando en contextInitialized");

        try {

            DatabaseConnectionManager.getInstance();

            logger.info("Conexión con la base de datos inicializada");

            InputStream sqlFileStream =
                    sce.getServletContext()
                            .getResourceAsStream("/WEB-INF/classes/data.sql");

            if (sqlFileStream == null) {
                logger.error("No se ha encontrado data.sql");
                return;
            }

            DataInitializer.loadDataFromSQL(sqlFileStream);

            logger.info("Datos iniciales cargados correctamente");

        } catch (Exception e) {

            logger.error("Error al inicializar la aplicación", e);
        }

        logger.info("Saliendo de contextInitialized");
    }

    /**
     * Método ejecutado al detener la aplicación.
     *
     * @param sce evento del contexto de la aplicación
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        logger.info("Entrando en contextDestroyed");

        logger.info("Aplicación detenida");

        logger.info("Saliendo de contextDestroyed");
    }
}