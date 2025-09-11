package Controlador;

import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

    public class GestorPacienteControl implements ActionListener {
    private Modelo.GestorPaciente pacientesModelo;
    private Vista.ConsPacienteInternalFrame consultarPacienteVista;
    
    public GestorPacienteControl(Vista.ConsPacienteInternalFrame consultarPacienteVista) {
        this.consultarPacienteVista = consultarPacienteVista;
        this.pacientesModelo = new Modelo.GestorPaciente();

        
        // Mostrar todos los pacientes al inicializar
        mostrarTodosPacientes();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // Solo maneja el botón "Aceptar"
        if (e.getSource() == consultarPacienteVista.btn_aceptar) {
            buscarPacientes();
        }
    }
    
    /**
     * Busca pacientes según el criterio seleccionado
     * Si no hay valor de búsqueda, muestra todos los pacientes
     */
    private void buscarPacientes() {
        
        String valor = consultarPacienteVista.txt_valor.getText().trim();
        
        // Si no hay valor, mostrar todos los pacientes
        if (valor.isEmpty()) {
            mostrarTodosPacientes();
            return;
        }
        /*
        List<Modelo.Paciente> pacientes = 
                pacientesModelo.buscarPacientesPorParametro(parametro, valor)
        */
        Modelo.GestorPaciente.Parametro parametro = obtenerParametroSeleccionado();
        
        if (parametro == null) {
            JOptionPane.showMessageDialog(consultarPacienteVista, 
                "Seleccione un parámetro de búsqueda (Identificación, Nombre, Apellidos o Sexo)");
            return;
        }
        
        List<Modelo.Paciente> pacientes = 
            pacientesModelo.buscarPacientesPorParametro(parametro, valor);
        
        actualizarTabla(pacientes);
        
        // Mostrar mensaje informativo
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(consultarPacienteVista, 
                "No se encontraron pacientes con el criterio: " + valor);
        } else {
            // Opcional: Mostrar cantidad encontrada
            System.out.println("Se encontraron " + pacientes.size() + " paciente(s)");
        }
    }
    
    /**
     * Muestra todos los pacientes en la tabla
     */
    private void mostrarTodosPacientes() {
        List<Modelo.Paciente> pacientes = pacientesModelo.obtenerTodosPacientes();
        actualizarTabla(pacientes);
        
        if (pacientes.isEmpty()) {
            JOptionPane.showMessageDialog(consultarPacienteVista, 
                "No hay pacientes registrados en el sistema");
        }
    }
    
    /**
     * Obtiene el parámetro de búsqueda seleccionado por el usuario
     */
    private Modelo.GestorPaciente.Parametro obtenerParametroSeleccionado() {
        if (consultarPacienteVista.rdb_identificacion.isSelected()) { 
            return Modelo.GestorPaciente.Parametro.IDENTIFICACION;
        }
        if (consultarPacienteVista.rdb_nombres.isSelected()) { 
            return Modelo.GestorPaciente.Parametro.NOMBRES;
        }
        if (consultarPacienteVista.rdb_apellidos.isSelected()) { 
            return Modelo.GestorPaciente.Parametro.APELLIDOS;
        }
        if (consultarPacienteVista.rdb_genero.isSelected()) { 
            return Modelo.GestorPaciente.Parametro.GENERO;
        }
        return null; // Ningún parámetro seleccionado
    }
    
    /**
     * Actualiza la tabla con la lista de pacientes proporcionada
     */
    private void actualizarTabla(List<Modelo.Paciente> pacientes) {
        String[] titulos = {"Identificación", "Nombres", "Apellidos", "Fecha Nacimiento", "Género"};
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(titulos);
        
        for (Modelo.Paciente p : pacientes) {
            String[] registro = new String[5];
            registro[0] = p.getIdentificacion();
            registro[1] = p.getNombres();
            registro[2] = p.getApellidos();
            registro[3] = p.getFechaNacimiento();
            registro[4] = p.getGenero();
            modelo.addRow(registro);
        }
        
        consultarPacienteVista.Tbl_datos.setModel(modelo);
    }
    
    /**
     * Método público para refrescar los datos (puede ser llamado desde otras partes)
     */
    public void refrescarDatos() {
        mostrarTodosPacientes();
    }
    
    /**
     * Método público para limpiar la búsqueda y mostrar todos
     */
    public void limpiarBusqueda() {
        consultarPacienteVista.txt_valor.setText("");
        mostrarTodosPacientes();
    }
}