package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;


public class PacienteControl implements ActionListener {
    private Vista.RegPacienteInternalFrame pacienteVista;
    private Modelo.Paciente pacienteModelo;
    private Modelo.GestorPaciente gestorPacienteModelo;
    
    public PacienteControl(Vista.RegPacienteInternalFrame pacienteVista) {
        this.pacienteVista = pacienteVista;
        this.gestorPacienteModelo = new Modelo.GestorPaciente();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón registrar
        if (e.getSource() == pacienteVista.btn_registrar) {
            if (validarCampos()) {
                registrarPaciente();
            }
        }
        
        
        // Botón nuevo/limpiar
        if (e.getSource() == pacienteVista.btn_nuevo) {
            limpiarFormulario();
        }
    }
    
    private boolean validarCampos() {
        // Validar identificación
        String identificacion = pacienteVista.txt_identificacion.getText().trim();
        if (identificacion.isEmpty()) {
            JOptionPane.showMessageDialog(pacienteVista, "La identificación es obligatoria");
            pacienteVista.txt_identificacion.requestFocus();
            return false;
        }
        
        // Validar nombres
        String nombres = pacienteVista.txt_nombres.getText().trim();
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(pacienteVista, "Los nombres son obligatorios");
            pacienteVista.txt_nombres.requestFocus();
            return false;
        }
        
        // Validar apellidos
        String apellidos = pacienteVista.txt_apellidos.getText().trim();
        if (apellidos.isEmpty()) {
            JOptionPane.showMessageDialog(pacienteVista, "Los apellidos son obligatorios");
            pacienteVista.txt_apellidos.requestFocus();
            return false;
        }
        
        // Validar fecha de nacimiento
        if (pacienteVista.Dtd_fecha_nacimiento.getDate() == null) {
            JOptionPane.showMessageDialog(pacienteVista, "La fecha de nacimiento es obligatoria");
            pacienteVista.Dtd_fecha_nacimiento.requestFocus();
            return false;
        }
        
        // Validar género
        if (!pacienteVista.rdb_masculino.isSelected() && !pacienteVista.rdb_femenino.isSelected()) {
            JOptionPane.showMessageDialog(pacienteVista, "Debe seleccionar un género");
            return false;
        }
        
        return true;
    }
    
    private void registrarPaciente() {
        try {
            String identificacion = pacienteVista.txt_identificacion.getText().trim();
            String nombres = pacienteVista.txt_nombres.getText().trim();
            String apellidos = pacienteVista.txt_apellidos.getText().trim();
            
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fecha_nacimiento = formato.format(pacienteVista.Dtd_fecha_nacimiento.getDate());
            
            String genero = pacienteVista.rdb_masculino.isSelected() ? "M" : "F";
            
            pacienteModelo = new Modelo.Paciente(
                identificacion, nombres, apellidos, fecha_nacimiento, genero
            );
            
            boolean registrado = gestorPacienteModelo.registrarPaciente(pacienteModelo);
            
            if (registrado) {
                limpiarFormulario();
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(pacienteVista, 
                "Error al registrar paciente: " + ex.getMessage());
        }
    }
    
    private void limpiarFormulario() {
        pacienteVista.txt_identificacion.setText("");
        pacienteVista.txt_nombres.setText("");
        pacienteVista.txt_apellidos.setText("");
        pacienteVista.Dtd_fecha_nacimiento.setDate(null);
        pacienteVista.rdb_masculino.setSelected(true);
        pacienteVista.rdb_femenino.setSelected(false);
        pacienteVista.txt_identificacion.requestFocus();
    }
}