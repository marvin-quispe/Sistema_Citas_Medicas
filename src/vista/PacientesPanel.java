package vista;

import controlador.PacienteControlador;
import modelo.Paciente;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class PacientesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable tablaPacientes;
    private JTextField txtBuscar;

    public PacientesPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        // ── TÍTULO ───────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Registro de Pacientes");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel(
                "Gestión de pacientes — RF-04 a RF-08");
        lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(16, 34, 300, 16);
        add(lblSubtitulo);

        // ── BUSCADOR ─────────────────────────────────────────────────
        JLabel lblBuscar = new JLabel("Buscar:");
        lblBuscar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblBuscar.setBounds(16, 62, 50, 22);
        add(lblBuscar);

        txtBuscar = new JTextField();
        txtBuscar.setBounds(66, 62, 200, 22);
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                buscarPaciente();
            }
        });
        add(txtBuscar);

        // ── BOTÓN NUEVO PACIENTE ─────────────────────────────────────
        JButton btnNuevo = new JButton("+ Nuevo Paciente");
        btnNuevo.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnNuevo.setBackground(new Color(26, 95, 168));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setOpaque(true);
        btnNuevo.setBorderPainted(false);
        btnNuevo.setBounds(460, 60, 90, 26);
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ── ABRE FORMULARIO REGISTRO ─────────────────────────
                RegistroPacienteDialog dialog =new RegistroPacienteDialog(null);
                dialog.setVisible(true);
                cargarTabla();
            }
        });
        add(btnNuevo);

        // ── TABLA ────────────────────────────────────────────────────
        tablaPacientes = new JTable();
        tablaPacientes.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaPacientes.setRowHeight(22);
        tablaPacientes.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tablaPacientes.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(tablaPacientes);
        scroll.setBounds(16, 96, 680, 460);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        // ── BOTONES INFERIORES ───────────────────────────────────────
        JButton btnEditar = new JButton("Editar");
        btnEditar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnEditar.setBackground(new Color(39, 174, 96));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setOpaque(true);
        btnEditar.setBorderPainted(false);
        btnEditar.setBounds(16, 564, 100, 26);
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaPacientes.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un paciente.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaPacientes.getModel()
                        .getValueAt(fila, 0);
                Paciente p = PacienteControlador
                        .buscarPorId(id);
                if (p == null) return;
                RegistroPacienteDialog dialog =
                        new RegistroPacienteDialog(null, p);
                dialog.setVisible(true);
                cargarTabla();
            }
        });
        add(btnEditar);
        
 
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnEliminar.setBackground(new Color(192, 57, 43));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setOpaque(true);
        btnEliminar.setBorderPainted(false);
        btnEliminar.setBounds(124, 564, 100, 26);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ── ELIMINAR PACIENTE SELECCIONADO ───────────────────
                int fila = tablaPacientes.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un paciente de la tabla.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaPacientes.getModel()
                        .getValueAt(fila, 0);
                
                
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "¿Está seguro de eliminar este paciente?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                String resultado =
                        PacienteControlador.eliminar(id);
                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("ERROR: ", ""),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(btnEliminar);
        

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnRefrescar.setBounds(232, 564, 100, 26);
        btnRefrescar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtBuscar.setText("");
                cargarTabla();
            }
        });
        add(btnRefrescar);

        // ── CARGA INICIAL ────────────────────────────────────────────
        cargarTabla();
    }

    private void cargarTabla() {
        String[] columnas = {"ID", "Nombre", "DNI",
                "Edad", "Sexo", "Teléfono", "Seguro"};
        DefaultTableModel model =
                new DefaultTableModel(columnas, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Paciente> lista =
                PacienteControlador.listar();
        for (Paciente p : lista) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNombreCompleto(),
                p.getDni(),
                modelo.Persona.calcularEdad(p.getFechaNacimiento()),
                p.getSexo(),
                p.getTelefono(),
                p.getTipoSeguro()
            });
        }
        tablaPacientes.setModel(model);
        tablaPacientes.getColumnModel()
                .getColumn(0).setMaxWidth(40);
    }

    private void buscarPaciente() {
        String texto = txtBuscar.getText().trim();
        String[] columnas = {"ID", "Nombre", "DNI",
                "Edad", "Sexo", "Teléfono", "Seguro"};
        DefaultTableModel model =
                new DefaultTableModel(columnas, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Paciente> lista =
                PacienteControlador.buscarPorNombreODni(texto);
        for (Paciente p : lista) {
            model.addRow(new Object[]{
                p.getId(),
                p.getNombreCompleto(),
                p.getDni(),
                modelo.Persona.calcularEdad(p.getFechaNacimiento()),
                p.getSexo(),
                p.getTelefono(),
                p.getTipoSeguro()
            });
        }
        tablaPacientes.setModel(model);
    }
}