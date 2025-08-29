package dao;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listener para gestión del ciclo de vida de recursos JPA y JDBC.
 */
@WebListener
public class JpaContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(JpaContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Inicializando contexto de la aplicación - Configuración JPA/JDBC");
        // No es necesario registrar drivers manualmente en JDBC 4.0+
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Iniciando limpieza de recursos al destruir el contexto");

        // 1. Cerrar todos los EntityManagerFactory
        cerrarRecursosJPA();

        // 2. Desregistrar drivers JDBC
        desregistrarDriversJDBC();

        LOGGER.info("Limpieza de recursos completada");
    }

    private void cerrarRecursosJPA() {
        try {
            LOGGER.info("Cerrando todos los EntityManagerFactory en caché");
            JpaPadre.closeAllEntityManagerFactories();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error durante el cierre de recursos JPA", e);
        }
    }

    private void desregistrarDriversJDBC() {
        LOGGER.info("Desregistrando drivers JDBC");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (esDriverDeAplicacion(driver)) {
                desregistrarDriver(driver);
            }
        }
    }

    private boolean esDriverDeAplicacion(Driver driver) {
        return driver.getClass().getClassLoader() == getClass().getClassLoader();
    }

    private void desregistrarDriver(Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
            LOGGER.log(Level.INFO, "Driver desregistrado: {0}", driver.getClass().getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al desregistrar driver: " + driver.getClass().getName(), e);
        }
    }
}