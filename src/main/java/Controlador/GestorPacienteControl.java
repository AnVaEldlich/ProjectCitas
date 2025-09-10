package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador para la gestión y consulta de pacientes
 * Maneja la lógica de búsqueda y visualización de pacientes en tabla
 */
public class GestorPacienteControl implements ActionListener {
    
    // Constantes para los tipos de parámetros de búsqueda
    private static final int PARAM_IDENTIFICACION = 1;
    private static final int PARAM_NOMBRES = 2;
    private static final int PARAM_APELLIDOS = 3;
    private static final int PARAM_GENERO = 4;
    
    // Títulos de las columnas de la tabla
    private static final String[] TITULOS_COLUMNAS = {
        "Identificación", "Nombres", "Apellidos", "Fecha Nacimiento", "Género"
    };
    
    private final Modelo.GestorPaciente pacientesModelo;
    private final Vista.ConsPacienteInternalFrame consultarPacienteVista;

    /**
     * Constructor del controlador
     * @param consultarPacienteVista Vista de consulta de pacientes
     */
    public GestorPacienteControl(Vista.ConsPacienteInternalFrame consultarPacienteVista) {
        this.consultarPacienteVista = consultarPacienteVista;
        this.pacientesModelo = new Modelo.GestorPaciente();
        
        // Cargar todos los pacientes al inicializar
        cargarTodosLosPacientes();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // Determinar si es búsqueda o mostrar todos
            if (e.getSource() == consultarPacienteVista.btn_buscar) {
                buscarPacientes();
            } else if (e.getSource() == consultarPacienteVista.btn_mostrar_todos) {
                cargarTodosLosPacientes();
            } else if (e.getSource() == consultarPacienteVista.btn_limpiar) {
                limpiarBusqueda();
            }
        } catch (Exception ex) {
            mostrarError("Error en la operación: " + ex.getMessage());
        }
    }

    /**
     * Busca pacientes según los criterios seleccionados
     */
    private void buscarPacientes() {
        String valor = obtenerValorBusqueda();
        
        if (valor.isEmpty()) {
            mostrarAdvertencia("Por favor ingrese un valor para buscar");
            return;
        }

        Modelo.GestorPaciente.Parametro parametro = obtenerParametroSeleccionado();
        
        if (parametro == null) {
            mostrarAdvertencia("Por favor seleccione un criterio de búsqueda");
            return;
        }

        List<Modelo.Paciente> pacientesEncontrados = pacientesModelo.buscarPacientesPorParametro(parametro, valor);
        
        if (pacientesEncontrados.isEmpty()) {
            mostrarInformacion("No se encontraron pacientes con el criterio especificado");
        }
        
        actualizarTabla(pacientesEncontrados);
    }

    /**
     * Carga todos los pacientes en la tabla
     */
    private void cargarTodosLosPacientes() {
        List<Modelo.Paciente> todosPacientes = pacientesModelo.obtenerTodosPacientes();
        actualizarTabla(todosPacientes);
        
        if (todosPacientes.isEmpty()) {
            mostrarInformacion("No hay pacientes registrados en el sistema");
        }
    }

    /**
     * Limpia los campos de búsqueda y recarga todos los pacientes
     */
    private void limpiarBusqueda() {
        consultarPacienteVista.txt_valor.setText("");
        
        // Deseleccionar todos los radio buttons
        consultarPacienteVista.buttonGroup.clearSelection();
        
        // Seleccionar por defecto el primer radio button
        consultarPacienteVista.rdb_identificacion.setSelected(true);
        
        // Recargar todos los pacientes
        cargarTodosLosPacientes();
        
        // Enfocar el campo de texto
        consultarPacienteVista.txt_valor.requestFocus();
    }

    /**
     * Obtiene el valor de búsqueda del campo de texto
     * @return Valor de búsqueda limpio (sin espacios al inicio y final)
     */
    private String obtenerValorBusqueda() {
        return consultarPacienteVista.txt_valor.getText().trim();
    }

    /**
     * Determina qué parámetro de búsqueda está seleccionado
     * @return Parámetro seleccionado o null si no hay ninguno seleccionado
     */
    private Modelo.GestorPaciente.Parametro obtenerParametroSeleccionado() {
        if (consultarPacienteVista.rdb_identificacion.isSelected()) {
            return Modelo.GestorPaciente.Parametro.IDENTIFICACION;
        } else if (consultarPacienteVista.rdb_nombres.isSelected()) {
            return Modelo.GestorPaciente.Parametro.NOMBRES;
        } else if (consultarPacienteVista.rdb_apellidos.isSelected()) {
            return Modelo.GestorPaciente.Parametro.APELLIDOS;
        } else if (consultarPacienteVista.rdb_genero.isSelected()) {
            return Modelo.GestorPaciente.Parametro.GENERO;
        }
        return null;
    }

    /**
     * Actualiza la tabla con la lista de pacientes proporcionada
     * @param pacientes Lista de pacientes a mostrar
     */
    private void actualizarTabla(List<Modelo.Paciente> pacientes) {
        DefaultTableModel modelo = crearModeloTabla();
        
        for (Modelo.Paciente paciente : pacientes) {
            String[] fila = crearFilaPaciente(paciente);
            modelo.addRow(fila);
        }
        
        consultarPacienteVista.tbl_datos.setModel(modelo);
        
        // Ajustar el ancho de las columnas automáticamente
        ajustarAnchoColumnas();
        
        // Mostrar información sobre la cantidad de resultados
        mostrarInfoResultados(pacientes.size());
    }

    /**
     * Crea un nuevo modelo de tabla con los títulos de columnas
     * @return Modelo de tabla configurado
     */
    private DefaultTableModel crearModeloTabla() {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Hacer que las celdas no sean editables
                return false;
            }
        };
        modelo.setColumnIdentifiers(TITULOS_COLUMNAS);
        return modelo;
    }

    /**
     * Crea una fila con los datos del paciente
     * @param paciente Paciente del cual extraer los datos
     * @return Array con los datos del paciente para la fila
     */
    private String[] crearFilaPaciente(Modelo.Paciente paciente) {
        return new String[] {
            paciente.getIdentificacion() != null ? paciente.getIdentificacion() : "",
            paciente.getNombres() != null ? paciente.getNombres() : "",
            paciente.getApellidos() != null ? paciente.getApellidos() : "",
            paciente.getFechaNacimiento() != null ? paciente.getFechaNacimiento() : "",
            paciente.getGenero() != null ? paciente.getGenero() : ""
        };
    }

    /**
     * Ajusta automáticamente el ancho de las columnas
     */
    private void ajustarAnchoColumnas() {
        try {
            consultarPacienteVista.tbl_datos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        } catch (Exception e) {
            // Si falla el ajuste automático, continuar sin él
            System.out.println("No se pudo ajustar el ancho de las columnas: " + e.getMessage());
        }
    }

    /**
     * Muestra información sobre la cantidad de resultados encontrados
     * @param cantidad Número de resultados
     */
    private void mostrarInfoResultados(int cantidad) {
        if (consultarPacienteVista.lbl_info != null) {
            String mensaje = switch (cantidad) {
                case 0 -> "No se encontraron resultados";
                case 1 -> "Se encontró 1 paciente";
                default -> "Se encontraron " + cantidad + " pacientes";
            };
            consultarPacienteVista.lbl_info.setText(mensaje);
        }
    }

    /**
     * Muestra un mensaje de error
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            consultarPacienteVista,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Muestra un mensaje de advertencia
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
            consultarPacienteVista,
            mensaje,
            "Advertencia",
            JOptionPane.WARNING_MESSAGE
        );
    }

    /**
     * Muestra un mensaje informativo
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarInformacion(String mensaje) {
        JOptionPane.showMessageDialog(
            consultarPacienteVista,
            mensaje,
            "Información",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Obtiene la cantidad total de pacientes registrados
     * @return Número de pacientes en el sistema
     */
    public int getCantidadPacientesTotal() {
        return pacientesModelo.getCantidadPacientes();
    }

    /**
     * Refresca la vista con los datos más actuales
     */
    public void refrescarVista() {
        cargarTodosLosPacientes();
    }
}