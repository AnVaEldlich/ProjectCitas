package Recursos;

import Recursos.Conexion;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Probar la conexión
        Conexion.probarConexion();
        Conexion.probarInsert();
        
        // Usar la conexión para consultas
        Connection conn = Conexion.getConnection();
        if (conn != null) {
            System.out.println("Conexión lista para usar");
            // Aquí puedes ejecutar tus consultas SQL
            // PreparedStatement, ResultSet, etc.
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}