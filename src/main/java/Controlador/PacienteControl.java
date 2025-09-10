package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class PacienteControl implements ActionListener {

    private Vista.RegPacienteInternalFrame pacienteVista;
    private Modelo.Paciente pacienteModelo;
    private Modelo.GestorPaciente gestorPacienteModelo;
    private Object Modelo;

    public PacienteControl(Vista.RegPacienteInternalFrame pacienteVista) {
        this.pacienteVista = pacienteVista;
        this.gestorPacienteModelo = new Modelo.GestorPaciente();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Botón guardar
        if (e.getSource() == pacienteVista.btn_registrar) {
            String identificacion = pacienteVista.txt_identificacion.getText();
            String nombres = pacienteVista.txt_nombres.getText();
            String apellidos = pacienteVista.txt_apellidos.getText();

            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            String fecha_nacimiento = formato.format(pacienteVista.Dtd_fecha_nacimiento.getDate());

            String genero = "";
            if (pacienteVista.rdb_masculino.isSelected()) {
                genero = "M";
            } else if (pacienteVista.rdb_femenino.isSelected()) {
                genero = "F";
            }

            pacienteModelo = new Modelo.Paciente(
                identificacion, nombres, apellidos, fecha_nacimiento, genero
            );

            gestorPacienteModelo.RegistrarPacientes(pacienteModelo);
        }

        // Botón limpiar
        if (e.getSource() == pacienteVista.btn_nuevo) {
            limpiarFormulario();
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
