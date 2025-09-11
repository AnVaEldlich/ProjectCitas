package Recursos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;



public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_medico";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = ""; 

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            return null;
        }
    }

    public Connection getConexion() {
        return getConnection();
    }

    public static void probarConexion() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("¡Conexión exitosa!");
                JOptionPane.showMessageDialog(null, "¡Conexión exitosa a la base de datos!");
            } else {
                System.out.println("Error al conectar");
                JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void probarInsert() {
        String sql = "INSERT INTO pacientes (PacIdentificacion, PacNombre, PacApellidos, PacFechaNacimiento, PacSexo) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "123456789");
            ps.setString(2, "Juan");
            ps.setString(3, "Pérez");
            ps.setDate(4, java.sql.Date.valueOf("1990-05-15"));
            ps.setString(5, "M");

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Se insertó un paciente de prueba.");
                JOptionPane.showMessageDialog(null, "✅ Se insertó un paciente de prueba en la tabla.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Error al insertar: " + e.getMessage());
        }
    }
}
