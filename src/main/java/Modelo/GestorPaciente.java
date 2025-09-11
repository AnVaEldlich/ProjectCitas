package Modelo;

import Recursos.Conexion;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class GestorPaciente {
    private LinkedList<Paciente> pacientes; 
    private Connection conn;

    public enum Parametro {
        IDENTIFICACION,
        NOMBRES,
        APELLIDOS,
        GENERO
    }

    // Constructor
    public GestorPaciente() {
        Conexion conexion = new Conexion();
        this.conn = conexion.getConexion();
        this.pacientes = new LinkedList<>();
        cargarPacientesDesdeDB();
    }

    // Cargar pacientes desde la base de datos
    private void cargarPacientesDesdeDB() {
        String sql = "SELECT PacIdentificacion, PacNombre, PacApellidos, PacFechaNacimiento, PacSexo FROM pacientes";
        try (PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            pacientes.clear();
            while (rs.next()) {
                Paciente paciente = new Paciente(
                    rs.getString("PacIdentificacion"),
                    rs.getString("PacNombre"),
                    rs.getString("PacApellidos"),
                    rs.getDate("PacFechaNacimiento").toString(),
                    rs.getString("PacSexo")
                );
                pacientes.add(paciente);
            }
            System.out.println("Cargados " + pacientes.size() + " pacientes desde la base de datos");
        } catch (SQLException e) {
            System.err.println("Error al cargar pacientes: " + e.getMessage());
        }
    }

    // Registrar paciente
    public boolean registrarPaciente(Paciente paciente) {
        if (paciente == null) return false;

        if (existePaciente(paciente.getIdentificacion())) {
            JOptionPane.showMessageDialog(null,
                "Ya existe un paciente con la identificación: " + paciente.getIdentificacion());
            return false;
        }

        String sql = "INSERT INTO pacientes (PacIdentificacion, PacNombre, PacApellidos, PacFechaNacimiento, PacSexo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, paciente.getIdentificacion());
            pst.setString(2, paciente.getNombres());
            pst.setString(3, paciente.getApellidos());
            pst.setDate(4, java.sql.Date.valueOf(convertirFecha(paciente.getFechaNacimiento())));
            pst.setString(5, paciente.getGenero());

            int rows = pst.executeUpdate();
            if (rows > 0) {
                pacientes.add(paciente);
                JOptionPane.showMessageDialog(null, "Paciente registrado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar paciente: " + e.getMessage());
        }
        return false;
    }

    // Conversión de fecha dd/MM/yyyy → yyyy-MM-dd
    private String convertirFecha(String fechaOriginal) {
        try {
            String[] partes = fechaOriginal.split("/");
            if (partes.length == 3) {
                return partes[2] + "-" +
                       String.format("%02d", Integer.parseInt(partes[1])) + "-" +
                       String.format("%02d", Integer.parseInt(partes[0]));
            }
        } catch (Exception e) {
            System.err.println("Error al convertir fecha: " + e.getMessage());
        }
        return fechaOriginal;
    }

    // Verificar existencia
    private boolean existePaciente(String identificacion) {
        return pacientes.stream()
                .anyMatch(p -> p.getIdentificacion() != null &&
                               p.getIdentificacion().equalsIgnoreCase(identificacion));
    }

    // Buscar pacientes
    public List<Paciente> buscarPacientesPorParametro(Parametro parametro, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return new LinkedList<>();
        }
        cargarPacientesDesdeDB();
        return pacientes.stream()
                .filter(p -> switch (parametro) {
                    case IDENTIFICACION -> p.getIdentificacion() != null &&
                                           p.getIdentificacion().toLowerCase().contains(valor.toLowerCase());
                    case NOMBRES -> p.getNombres() != null &&
                                    p.getNombres().toLowerCase().contains(valor.toLowerCase());
                    case APELLIDOS -> p.getApellidos() != null &&
                                      p.getApellidos().toLowerCase().contains(valor.toLowerCase());
                    case GENERO -> p.getGenero() != null &&
                                   p.getGenero().equalsIgnoreCase(valor.trim());
                })
                .collect(Collectors.toList());
    }

    // Obtener todos
    public List<Paciente> obtenerTodosPacientes() {
        cargarPacientesDesdeDB();
        return new LinkedList<>(pacientes);
    }

    // Eliminar paciente
    public boolean eliminarPaciente(String identificacion) {
        String sql = "DELETE FROM pacientes WHERE PacIdentificacion = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, identificacion);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                pacientes.removeIf(p -> p.getIdentificacion().equalsIgnoreCase(identificacion));
                JOptionPane.showMessageDialog(null, "Paciente eliminado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar paciente: " + e.getMessage());
        }
        return false;
    }

    public int getCantidadPacientes() {
        cargarPacientesDesdeDB();
        return pacientes.size();
    }

    // Cerrar conexión
    public void cerrarConexion() {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
