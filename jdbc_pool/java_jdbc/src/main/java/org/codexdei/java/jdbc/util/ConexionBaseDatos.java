package org.codexdei.java.jdbc.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


//Se implementa el patron creacional Singleton implementacion Holder
public class ConexionBaseDatos {

    private static String url = "jdbc:mysql://localhost:3306/java_curso";
    private static String user = "root";
    private static String password = "admin";
    private static BasicDataSource pool;

    //Constructor privado para evitar instancias externas
    private ConexionBaseDatos(){};

    // Método para crear la conexión
        private static BasicDataSource getInstance() throws SQLException {

            if (pool == null){

                pool = new BasicDataSource();
                pool.setUrl(url);
                pool.setUsername(user);
                pool.setPassword(password);
                pool.setMinIdle(3);
                pool.setMaxIdle(8);
                pool.setMaxTotal(8);
            }
            return pool;
        }

    // Método público para obtener la instancia de la conexión
    public static Connection getConnection() throws SQLException {

        return getInstance().getConnection();
    }

}
