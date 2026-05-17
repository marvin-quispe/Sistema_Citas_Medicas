package vista;

import controlador.Autorizador;
import controlador.CitaControlador;
import controlador.MedicoControlador;
import controlador.PacienteControlador;
import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import modelo.Usuario;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HistorialPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaHistorial;
    private JComboBox<Paciente> cmbPaciente;
    private Usuario usuarioSesion;

    public HistorialPanel(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        boolean esDoc = Autorizador.esMedico(usuario);

        JLabel lblTitulo = new JLabel("Historial Clínico");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        Medico medicoAsociado = null;
        if (esDoc && usuario.tieneEnlaceEntidad()) {
            medicoAsociado = MedicoControlador.buscarPorId(usuario.getEntidadId());
        }
        final Medico medicoFinal = medicoAsociado;

        if (esDoc && medicoAsociado != null) {
            JLabel lblInfo = new JLabel("Dr. " + medicoAsociado.getNombreCompleto() + " — Citas atendidas");
            lblInfo.setFont(new Font("Tahoma", Font.PLAIN, 11));
            lblInfo.setForeground(Color.GRAY);
            lblInfo.setBounds(16, 34, 350, 16);
            add(lblInfo);
        }

        int formY = esDoc ? 58 : 62;
        boolean showPacienteFilter = !esDoc;

        if (showPacienteFilter) {
            JLabel lblPac = new JLabel("Paciente:");
            lblPac.setFont(new Font("Tahoma", Font.PLAIN, 11));
            lblPac.setBounds(16, formY, 70, 20);
            add(lblPac);

            cmbPaciente = new JComboBox<>();
            cmbPaciente.addItem(null);
            ArrayList<Paciente> pacs = PacienteControlador.listar();
            for (Paciente p : pacs) {
                cmbPaciente.addItem(p);
            }
            cmbPaciente.setBounds(86, formY, 220, 22);
            cmbPaciente.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cargarTabla(medicoFinal);
                }
            });
            add(cmbPaciente);

            JButton btnVerTodos = new JButton("Ver todos");
            btnVerTodos.setFont(new Font("Tahoma", Font.PLAIN, 11));
            btnVerTodos.setBounds(314, formY, 94, 22);
            btnVerTodos.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cmbPaciente.setSelectedIndex(0);
                    cargarTabla(medicoFinal);
                }
            });
            add(btnVerTodos);
        }

        tablaHistorial = new JTable();
        tablaHistorial.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaHistorial.setRowHeight(22);
        tablaHistorial.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBounds(16, formY + (showPacienteFilter ? 30 : 30), 430, 360);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        if (Autorizador.puedeEditar(usuario, "historial")) {
            JButton btnObs = new JButton("Registrar Observación");
            btnObs.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnObs.setBackground(new Color(26, 95, 168));
            btnObs.setForeground(Color.WHITE);
            btnObs.setOpaque(true);
            btnObs.setBorderPainted(false);
            btnObs.setBounds(16, 460, 180, 26);
            btnObs.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int fila = tablaHistorial.getSelectedRow();
                    if (fila == -1) {
                        JOptionPane.showMessageDialog(null, "Seleccione una cita atendida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = (int) tablaHistorial.getModel().getValueAt(fila, 0);
                    ObservacionesDialog dialog = new ObservacionesDialog(null, id);
                    dialog.setVisible(true);
                    cargarTabla(medicoFinal);
                }
            });
            add(btnObs);
        }

        cargarTabla(medicoFinal);
    }

    private void cargarTabla(Medico medicoFiltro) {
        String[] cols = {"ID", "Paciente", "Doctor", "Fecha", "Motivo", "Observaciones", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ArrayList<Cita> lista;
        if (Autorizador.esMedico(usuarioSesion) && medicoFiltro != null) {
            lista = CitaControlador.listarPorMedico(medicoFiltro.getId());
        } else {
            Paciente sel = (cmbPaciente != null) ? (Paciente) cmbPaciente.getSelectedItem() : null;
            if (sel == null) {
                lista = CitaControlador.listarPorEstado("Atendida");
            } else {
                lista = CitaControlador.listarPorPaciente(sel.getId());
            }
        }

        for (Cita c : lista) {
            if (!c.getEstado().equals("Atendida")) continue;
            model.addRow(new Object[]{
                c.getId(),
                c.getPaciente().getNombreCompleto(),
                c.getMedico().getNombreCompleto(),
                c.getFecha(),
                c.getMotivo(),
                c.getObservaciones().isEmpty() ? "Sin observaciones" : c.getObservaciones(),
                c.getEstado()
            });
        }
        tablaHistorial.setModel(model);
        tablaHistorial.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}
