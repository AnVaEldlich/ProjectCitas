package Modelo;

import Modelo.Paciente;
import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import Recursos.Conexion;   // ✅ ya importas la clase Conexion

        public class GestorPaciente {
            private static LinkedList<Paciente> pacientes;
            private static Connection conn;

            public GestorPaciente() {
                Conexion conexion = new Conexion("localhost", "XE", "SYSTEM", "123"); // ✅ sin "Recursos."
                conn = conexion.getConexion();
                this.pacientes = new LinkedList<>();
            }
        }


    // Fixed method name to match the one being called
    public void RegistrarPacientes(Paciente paciente) {
        if (paciente != null) {
            pacientes.add(paciente);
            System.out.println("Paciente registrado exitosamente: " + paciente.toString());
        } else {
            System.out.println("Error: No se puede registrar un paciente nulo");
        }
    }

    // Alternative method with standard naming convention
    public void registrarPaciente(Paciente paciente) {
        RegistrarPacientes(paciente);
    }

    public List<Paciente> buscarPacientesPorParametro(Parametro parametro, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return new LinkedList<>();
        }

        return pacientes.stream()
                .filter(pac -> switch (parametro) {
                    case IDENTIFICACION -> pac.getIdentificacion() != null && 
                                          pac.getIdentificacion().equalsIgnoreCase(valor.trim());
                    case NOMBRES -> pac.getNombres() != null && 
                                   pac.getNombres().equalsIgnoreCase(valor.trim());
                    case APELLIDOS -> pac.getApellidos() != null && 
                                     pac.getApellidos().equalsIgnoreCase(valor.trim());
                    case GENERO -> pac.getGenero() != null && 
                                  pac.getGenero().equalsIgnoreCase(valor.trim());
                })
                .collect(Collectors.toList());
    }

    public List<Paciente> obtenerTodosPacientes() {
        return new LinkedList<>(pacientes);
    }

    public boolean eliminarPaciente(String identificacion) {
        return pacientes.removeIf(pac -> pac.getIdentificacion() != null && 
                                        pac.getIdentificacion().equalsIgnoreCase(identificacion));
    }

    public int getCantidadPacientes() {
        return pacientes.size();
    }

    // Enum for parameters instead of magic numbers
    public enum Parametro {
        IDENTIFICACION,
        NOMBRES,
        APELLIDOS,
        GENERO
    }
}