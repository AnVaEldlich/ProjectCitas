package Controlador;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class GestorPacienteControl implements ActionListener {
    Modelo.GestorPaciente pacientesModelo;
    Vista.ConsPacienteInternalFrame consultarPacienteVista;
    
    public GestorPacienteControl(Vista.ConsPacienteInternalFrame consultarPacienteVista) {
        this.consultarPacienteVista = consultarPacienteVista;
        pacientesModelo = new Modelo.GestorPaciente();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultTableModel tmodelo;
        String valor = consultarPacienteVista.txt_valor.getText();
        Modelo.GestorPaciente.Parametro parametro = null;
        
        // Usar enum en lugar de números mágicos
        if (consultarPacienteVista.rdb_identificacion.isSelected()) { 
            parametro = Modelo.GestorPaciente.Parametro.IDENTIFICACION;
        }
        if (consultarPacienteVista.rdb_nombres.isSelected()) { 
            parametro = Modelo.GestorPaciente.Parametro.NOMBRES;
        }
        if (consultarPacienteVista.rdb_apellidos.isSelected()) { 
            parametro = Modelo.GestorPaciente.Parametro.APELLIDOS;
        }
        if (consultarPacienteVista.rdb_genero.isSelected()) { 
            parametro = Modelo.GestorPaciente.Parametro.GENERO;
        }
        
        // Validar que se haya seleccionado un parámetro
        if (parametro == null) {
            // Mostrar mensaje de error o seleccionar parámetro por defecto
            return;
        }
        
        LinkedList<Modelo.Paciente> pacientes = 
            (LinkedList<Modelo.Paciente>) pacientesModelo.buscarPacientesPorParametro(parametro, valor);
        
        String registro[] = new String[5];
        String[] Titulos = {"Identificacion", "Nombre", "Apellidos", "Fecha Nacimiento", "Genero"};
        tmodelo = new DefaultTableModel();
        tmodelo.setColumnIdentifiers(Titulos);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Modelo.Paciente p : pacientes) {
            registro[0] = p.getIdentificacion();
            registro[1] = p.getNombres();
            registro[2] = p.getApellidos();
            registro[3] = (p.getFechaNacimiento() != null) ? sdf.format(p.getFechaNacimiento()) : "";
            registro[4] = p.getGenero();
            tmodelo.addRow(registro);
        }
        
        consultarPacienteVista.Tbl_datos.setModel(tmodelo);
    }
}