package vista;

import controlador.CitaControlador;
import controlador.PagoControlador;
import modelo.Cita;
import modelo.Pago;
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

public class PagosPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaPendientes;
    private JTable tablaPagos;
    private JLabel lblMonto;

    public PagosPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Caja — Registro de Pagos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 300, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel(
                "El paciente paga ANTES de ir al consultorio");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 300, 16);
        add(lblSub);

        // ── SECCIÓN CITAS PENDIENTES DE PAGO ─────────────────────────
        JLabel lblPend = new JLabel("Citas pendientes de pago:");
        lblPend.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPend.setForeground(new Color(230, 126, 34));
        lblPend.setBounds(16, 60, 200, 20);
        add(lblPend);

        tablaPendientes = new JTable();
        tablaPendientes.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaPendientes.setRowHeight(22);
        tablaPendientes.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(230, 126, 34)));

        JScrollPane scrollPend = new JScrollPane(tablaPendientes);
        scrollPend.setBounds(16, 84, 680, 140);
        scrollPend.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scrollPend);

        JButton btnPagar = new JButton("Registrar Pago");
        btnPagar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnPagar.setBackground(new Color(39, 174, 96));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.setOpaque(true);
        btnPagar.setBorderPainted(false);
        btnPagar.setBounds(264, 232, 144, 26);
        btnPagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaPendientes.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione una cita pendiente.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaPendientes.getModel().getValueAt(fila, 0);
                Cita citaSel = CitaControlador.buscarPorId(id);
                if (citaSel == null) return;
                RegistroPagoDialog dialog = new RegistroPagoDialog(null, citaSel);
                dialog.setVisible(true);
                cargarDatos();
            }
        });
        add(btnPagar);

        // ── SECCIÓN PAGOS REGISTRADOS ────────────────────────────────
        JLabel lblPag = new JLabel("Pagos registrados:");
        lblPag.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPag.setForeground(new Color(39, 174, 96));
        lblPag.setBounds(16, 272, 200, 20);
        add(lblPag);

        tablaPagos = new JTable();
        tablaPagos.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaPagos.setRowHeight(22);
        tablaPagos.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tablaPagos.setEnabled(false);

        JScrollPane scrollPagos = new JScrollPane(tablaPagos);
        scrollPagos.setBounds(16, 296, 680, 180);
        scrollPagos.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scrollPagos);

        lblMonto = new JLabel("S/ 0.00");
        lblMonto.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblMonto.setForeground(new Color(39, 174, 96));
        lblMonto.setBounds(16, 484, 120, 20);
        add(lblMonto);

        JLabel lblTotalLbl = new JLabel("Total ingresos:");
        lblTotalLbl.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTotalLbl.setBounds(16, 464, 110, 20);
        add(lblTotalLbl);

        cargarDatos();
    }

    private void cargarDatos() {
        // ── TABLA PENDIENTES DE PAGO ─────────────────────────────────
        String[] colsPend = {"ID", "Paciente", "Doctor",
                "Especialidad", "Fecha", "Hora", "Motivo"};
        DefaultTableModel modelPend = new DefaultTableModel(colsPend, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        ArrayList<Cita> pendientes = CitaControlador.listarPorEstado("Pendiente");
        for (Cita c : pendientes) {
            if (!PagoControlador.citaYaPagada(c.getId())) {
                modelPend.addRow(new Object[]{
                    c.getId(),
                    c.getPaciente().getNombreCompleto(),
                    c.getMedico().getNombreCompleto(),
                    c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "—",
                    c.getFecha(),
                    c.getHora(),
                    c.getMotivo()
                });
            }
        }
        tablaPendientes.setModel(modelPend);
        tablaPendientes.getColumnModel().getColumn(0).setMaxWidth(40);

        // ── TABLA PAGOS REGISTRADOS ──────────────────────────────────
        String[] colsPagos = {"ID", "Paciente", "Doctor",
                "Fecha Pago", "Monto", "Método"};
        DefaultTableModel modelPagos = new DefaultTableModel(colsPagos, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        ArrayList<Pago> lista = PagoControlador.listarTodos();
        for (Pago p : lista) {
            modelPagos.addRow(new Object[]{
                p.getId(),
                p.getCita().getPaciente().getNombreCompleto(),
                p.getCita().getMedico().getNombreCompleto(),
                p.getFecha(),
                "S/ " + String.format("%.2f", p.getMonto()),
                p.getMetodoPago()
            });
        }
        tablaPagos.setModel(modelPagos);
        tablaPagos.getColumnModel().getColumn(0).setMaxWidth(40);

        lblMonto.setText("S/ " + String.format("%.2f", PagoControlador.totalIngresos()));
    }
}
