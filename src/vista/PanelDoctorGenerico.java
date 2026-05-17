package vista;

import controlador.CitaControlador;
import controlador.MedicoControlador;
import modelo.Cita;
import modelo.Medico;
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

/**
 * Panel para el rol "Médico" genérico.
 * Permite seleccionar cualquier doctor del sistema para ver sus citas
 * y atenderlas. En producción cada médico tendría su propio usuario,
 * pero para demostración basta con el rol.
 */
public class PanelDoctorGenerico extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaCitas;
    private JComboBox<Medico> cmbDoctor;
    private Usuario usuarioSesion;
    private JLabel lblInfoDoctor;

    public PanelDoctorGenerico(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        // ── TÍTULO ───────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Consultorio Médico");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 280, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel("Bienvenido, Doctor — Seleccione un médico para ver su agenda");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 400, 16);
        add(lblSub);

        // ── SELECTOR DE DOCTOR ───────────────────────────────────────
        JLabel lblSel = new JLabel("Seleccionar Doctor:");
        lblSel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSel.setBounds(16, 62, 130, 20);
        add(lblSel);

        cmbDoctor = new JComboBox<>();
        cmbDoctor.addItem(null);
        ArrayList<Medico> medicos = MedicoControlador.listar();
        for (Medico m : medicos) {
            cmbDoctor.addItem(m);
        }
        cmbDoctor.setBounds(150, 62, 220, 22);
        cmbDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        add(cmbDoctor);

        lblInfoDoctor = new JLabel("");
        lblInfoDoctor.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblInfoDoctor.setForeground(new Color(26, 95, 168));
        lblInfoDoctor.setBounds(380, 62, 200, 20);
        add(lblInfoDoctor);

        // ── TABLA DE CITAS ───────────────────────────────────────────
        tablaCitas = new JTable();
        tablaCitas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaCitas.setRowHeight(22);
        tablaCitas.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaCitas);
        scroll.setBounds(16, 92, 570, 340);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        // ── BOTONES ──────────────────────────────────────────────────
        JButton btnAtender = new JButton("Atender");
        btnAtender.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAtender.setBackground(new Color(39, 174, 96));
        btnAtender.setForeground(Color.WHITE);
        btnAtender.setOpaque(true);
        btnAtender.setBorderPainted(false);
        btnAtender.setBounds(16, 440, 100, 26);
        btnAtender.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaCitas.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una cita.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaCitas.getModel().getValueAt(fila, 0);
                String res = CitaControlador.atender(id);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null, res.replace("OK: ", ""), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(null, res.replace("ERROR: ", ""), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(btnAtender);

        JButton btnObs = new JButton("Observaciones");
        btnObs.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnObs.setBackground(new Color(26, 95, 168));
        btnObs.setForeground(Color.WHITE);
        btnObs.setOpaque(true);
        btnObs.setBorderPainted(false);
        btnObs.setBounds(124, 440, 120, 26);
        btnObs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaCitas.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una cita.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaCitas.getModel().getValueAt(fila, 0);
                ObservacionesDialog dialog = new ObservacionesDialog(null, id);
                dialog.setVisible(true);
                cargarTabla();
            }
        });
        add(btnObs);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] cols = {"ID", "Paciente", "Especialidad", "Fecha", "Hora", "Motivo", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        Medico sel = (Medico) cmbDoctor.getSelectedItem();
        if (sel != null) {
            lblInfoDoctor.setText(sel.getNombreCompleto() + " — " +
                (sel.getEspecialidad() != null ? sel.getEspecialidad().getNombre() : "Sin especialidad"));

            ArrayList<Cita> lista = CitaControlador.listarPorMedico(sel.getId());
            for (Cita c : lista) {
                model.addRow(new Object[]{
                    c.getId(),
                    c.getPaciente().getNombreCompleto(),
                    c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "—",
                    c.getFecha(),
                    c.getHora(),
                    c.getMotivo(),
                    c.getEstado()
                });
            }
        } else {
            lblInfoDoctor.setText("");
        }
        tablaCitas.setModel(model);
        tablaCitas.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}
