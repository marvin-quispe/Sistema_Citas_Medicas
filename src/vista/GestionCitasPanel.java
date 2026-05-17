package vista;

import controlador.Autorizador;
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

public class GestionCitasPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaCitas;
    private JComboBox<String> cmbFiltro;
    private Usuario usuarioSesion;

    public GestionCitasPanel(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        boolean esDoc = Autorizador.esMedico(usuario);

        JLabel lblTitulo = new JLabel(esDoc ? "Mi Agenda — Citas por Atender" : "Gestión de Citas");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 350, 25);
        add(lblTitulo);

        if (esDoc) {
            Medico med = MedicoControlador.buscarPorId(usuario.getEntidadId());
            if (med != null) {
                JLabel lblSub = new JLabel("Dr. " + med.getNombreCompleto() + " — Citas pagadas listas para atender");
                lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
                lblSub.setForeground(Color.GRAY);
                lblSub.setBounds(16, 34, 450, 16);
                add(lblSub);
            }
        }

        // ── FILTRO ───────────────────────────────────────────────────
        JLabel lblFiltro = new JLabel("Filtrar por estado:");
        lblFiltro.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFiltro.setBounds(16, esDoc ? 58 : 62, 120, 20);
        add(lblFiltro);

        // Para el doctor, por defecto muestra "Pagado" (las que puede atender)
        String filtroInicial = esDoc ? "Pagado" : "Todos";
        cmbFiltro = new JComboBox<>(new String[]{
            "Todos", "Pendiente", "Pagado", "Atendida",
            "Cancelada", "Reprogramada"});
        cmbFiltro.setSelectedItem(filtroInicial);
        cmbFiltro.setBounds(136, esDoc ? 58 : 62, 140, 22);
        cmbFiltro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        add(cmbFiltro);

        tablaCitas = new JTable();
        tablaCitas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaCitas.setRowHeight(22);
        tablaCitas.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaCitas);
        scroll.setBounds(16, esDoc ? 88 : 92, 430, 340);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        // ── BOTONES ──────────────────────────────────────────────────
        int btnY = esDoc ? 440 : 442;

        if (Autorizador.puedeEditar(usuario, "gestion_citas")) {
            JButton btnAtender = new JButton("Atender");
            btnAtender.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnAtender.setBackground(new Color(39, 174, 96));
            btnAtender.setForeground(Color.WHITE);
            btnAtender.setOpaque(true);
            btnAtender.setBorderPainted(false);
            btnAtender.setBounds(16, btnY, 100, 26);
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
        }

        JButton btnObs = new JButton("Observaciones");
        btnObs.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnObs.setBackground(new Color(26, 95, 168));
        btnObs.setForeground(Color.WHITE);
        btnObs.setOpaque(true);
        btnObs.setBorderPainted(false);
        int xObs = Autorizador.puedeEditar(usuario, "gestion_citas") ? 124 : 16;
        btnObs.setBounds(xObs, btnY, 120, 26);
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

        if (Autorizador.puedeEditar(usuario, "gestion_citas")) {
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnCancelar.setBackground(new Color(192, 57, 43));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setOpaque(true);
            btnCancelar.setBorderPainted(false);
            btnCancelar.setBounds(252, btnY, 100, 26);
            btnCancelar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int fila = tablaCitas.getSelectedRow();
                    if (fila == -1) {
                        JOptionPane.showMessageDialog(null, "Seleccione una cita.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = (int) tablaCitas.getModel().getValueAt(fila, 0);
                    String res = CitaControlador.cancelar(id);
                    if (res.startsWith("OK")) {
                        JOptionPane.showMessageDialog(null, res.replace("OK: ", ""), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        cargarTabla();
                    } else {
                        JOptionPane.showMessageDialog(null, res.replace("ERROR: ", ""), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            add(btnCancelar);
        }

        cargarTabla();
    }

    private void cargarTabla() {
        String filtro = (String) cmbFiltro.getSelectedItem();
        String[] cols = {"ID", "Paciente", "Doctor", "Especialidad", "Fecha", "Hora", "Motivo", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ArrayList<Cita> lista;
        if (Autorizador.esMedico(usuarioSesion)) {
            lista = CitaControlador.listarPorMedico(usuarioSesion.getEntidadId());
        } else {
            if (filtro == null || filtro.equals("Todos")) {
                lista = CitaControlador.listarTodas();
            } else {
                lista = CitaControlador.listarPorEstado(filtro);
            }
        }

        for (Cita c : lista) {
            model.addRow(new Object[]{
                c.getId(),
                c.getPaciente().getNombreCompleto(),
                c.getMedico().getNombreCompleto(),
                c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "—",
                c.getFecha(),
                c.getHora(),
                c.getMotivo(),
                c.getEstado()
            });
        }
        tablaCitas.setModel(model);
        tablaCitas.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}
