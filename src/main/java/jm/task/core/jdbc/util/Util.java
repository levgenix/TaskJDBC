package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // == JDBC

    private static Connection conn = null;
    private static SessionFactory sessionFactory = null;
    private static Metadata metadata = null;

    public static Connection getConnection() {
        try {
            if (null == conn || conn.isClosed()) {
                Properties props = getProps();
                conn = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.username"), props.getProperty("db.password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    private static Properties getProps() {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(Util.class.getResource("/database.properties").toURI()))) {
            props.load(in);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return props;
    }

    // == Hibernate

    public static SessionFactory getSessionFactory() {
        if (null == sessionFactory) {
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySetting(Environment.USE_SQL_COMMENTS, false)
                    .applySetting(Environment.SHOW_SQL, true)
                    .applySetting(Environment.HBM2DDL_AUTO, "update") //update create-drop none
                    .build();
            sessionFactory = makeSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }

    private static SessionFactory makeSessionFactory(ServiceRegistry serviceRegistry) {
        metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .getMetadataBuilder()
                .build();
                return  metadata.buildSessionFactory();
    }

    public static String getTableName(String entityName) throws NullPointerException {
        if (null == metadata) {
            throw new NullPointerException("Metadata is null");
        }

        for (PersistentClass persistentClass : Util.metadata.getEntityBindings()) {
            if (entityName.equals(persistentClass.getJpaEntityName())) {
                return persistentClass.getTable().getName();
            }
        }

        throw new NullPointerException(String.format("Entity {%s} not found", entityName));
    }
}
