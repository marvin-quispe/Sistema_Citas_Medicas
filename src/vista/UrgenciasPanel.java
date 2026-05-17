package vista;

import controlador.UrgenciaControlador;
import modelo.Urgencia;
import javax.swing.JPanel;
import javax.swing.JLabel;
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
import java.util.ArrayList;

public class UrgenciasPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaUrgencias;

    public UrgenciasPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Módulo de Urgencias");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(163, 45, 45));
        lblTitulo.setBounds(16, 11, 280, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel(
                "RF-28 a RF-29 — Atención inmediata sin cita previa");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 350, 16);
        add(lblSub);

        JButton btnNueva = new JButton("🚨 Nueva Urgencia");
        btnNueva.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnNueva.setBackground(new Color(163, 45, 45));
        btnNueva.setForeground(Color.WHITE);
        btnNueva.setOpaque(true);
        btnNueva.setBorderPainted(false);
        btnNueva.setBounds(256, 58, 150, 26);
        btnNueva.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistroUrgenciaDialog dialog =
                        new RegistroUrgenciaDialog(null);
                dialog.setVisible(true);
                cargarTabla();
            }
        });
        add(btnNueva);

        tablaUrgencias = new JTable();
        tablaUrgencias.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaUrgencias.setRowHeight(22);
        tablaUrgencias.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(163, 45, 45)));

        JScrollPane scroll = new JScrollPane(tablaUrgencias);
        scroll.setBounds(16, 92, 392, 380);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        JButton btnAlta = new JButton("Dar Alta");
        btnAlta.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAlta.setBackground(new Color(39, 174, 96));
        btnAlta.setForeground(Color.WHITE);
        btnAlta.setOpaque(true);
        btnAlta.setBorderPainted(false);
        btnAlta.setBounds(16, 480, 100, 26);
        btnAlta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaUrgencias.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione una urgencia.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaUrgencias.getModel()
                        .getValueAt(fila, 0);
                String res = UrgenciaControlador.darAlta(id);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            res.replace("OK: ", ""),
                            "Alta médica",
                            JOptionPane.INFORMATION_MESSAGE);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(null,
                            res.replace("ERROR: ", ""),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(btnAlta);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] cols = {"ID", "Paciente", "DNI",
                "Motivo", "Prioridad", "Doctor", "Estado"};
        DefaultTableModel model =
                new DefaultTableModel(cols, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Urgencia> lista =
                UrgenciaControlador.listarTodas();
        for (Urgencia u : lista) {
            model.addRow(new Object[]{
                u.getId(),
                u.getNombre(),
                u.getDni(),
                u.getMotivo(),
                u.getPrioridad(),
                u.getMedico() != null
                        ? u.getMedico().getNombreCompleto() : "—",
                u.getEstado()
            });
        }
        tablaUrgencias.setModel(model);
        tablaUrgencias.getColumnModel()
                .getColumn(0).setMaxWidth(40);
    }
}