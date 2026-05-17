package vista;

import controlador.CitaControlador;
import controlador.MedicoControlador;
import controlador.PagoControlador;
import controlador.UrgenciaControlador;
import modelo.Medico;
import modelo.Pago;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportesPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public ReportesPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Reportes del Sistema");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 280, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel(
                "RF-35 a RF-37 — Reportes de citas e ingresos");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 320, 16);
        add(lblSub);

        // ── TARJETAS RESUMEN ─────────────────────────────────────────
        agregarTarjeta(16, 60, "Total citas",
                String.valueOf(CitaControlador.totalCitas()),
                new Color(232, 241, 251),
                new Color(26, 95, 168));
        agregarTarjeta(110, 60, "Atendidas",
                String.valueOf(CitaControlador
                        .totalPorEstado("Atendida")),
                new Color(232, 248, 240),
                new Color(15, 110, 86));
        agregarTarjeta(204, 60, "Canceladas",
                String.valueOf(CitaControlador
                        .totalPorEstado("Cancelada")),
                new Color(253, 232, 232),
                new Color(163, 45, 45));
        agregarTarjeta(298, 60, "Urgencias",
                String.valueOf(UrgenciaControlador.totalActivas()),
                new Color(255, 248, 225),
                new Color(133, 79, 11));

        // ── TABLA RESUMEN POR MÉDICO ─────────────────────────────────
        JLabel lblMed = new JLabel("Resumen por médico");
        lblMed.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblMed.setForeground(new Color(26, 95, 168));
        lblMed.setBounds(16, 136, 200, 20);
        add(lblMed);

        JTable tablaMed = new JTable();
        tablaMed.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaMed.setRowHeight(22);
        tablaMed.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tablaMed.setEnabled(false);

        String[] colsMed = {"Doctor", "Especialidad",
                "Citas atendidas", "Ingresos"};
        DefaultTableModel modelMed =
                new DefaultTableModel(colsMed, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Medico> medicos = MedicoControlador.listar();
        ArrayList<Pago> pagos = PagoControlador.listarTodos();
        for (Medico m : medicos) {
            int atendidas = CitaControlador
                    .listarPorMedico(m.getId())
                    .stream()
                    .filter(c -> c.getEstado().equals("Atendida"))
                    .toArray().length;
            double ingresos = pagos.stream()
                    .filter(p -> p.getCita()
                            .getMedico().getId() == m.getId())
                    .mapToDouble(Pago::getMonto)
                    .sum();
            modelMed.addRow(new Object[]{
                m.getNombreCompleto(),
                m.getEspecialidad() != null
                        ? m.getEspecialidad().getNombre() : "—",
                atendidas,
                "S/ " + String.format("%.2f", ingresos)
            });
        }
        tablaMed.setModel(modelMed);

        JScrollPane scrollMed = new JScrollPane(tablaMed);
        scrollMed.setBounds(16, 158, 392, 150);
        scrollMed.setBorder(new LineBorder(
                new Color(200, 210, 220)));
        add(scrollMed);

        // ── TABLA INGRESOS POR MÉTODO ────────────────────────────────
        JLabel lblMet = new JLabel("Ingresos por método de pago");
        lblMet.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblMet.setForeground(new Color(26, 95, 168));
        lblMet.setBounds(16, 320, 250, 20);
        add(lblMet);

        JTable tablaMet = new JTable();
        tablaMet.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaMet.setRowHeight(22);
        tablaMet.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tablaMet.setEnabled(false);

        String[] colsMet = {"Método de pago",
                "Cantidad", "Total"};
        DefaultTableModel modelMet =
                new DefaultTableModel(colsMet, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        Map<String, double[]> metodos = new HashMap<>();
        for (Pago p : pagos) {
            String met = p.getMetodoPago();
            if (!metodos.containsKey(met)) {
                metodos.put(met, new double[]{0, 0});
            }
            metodos.get(met)[0]++;
            metodos.get(met)[1] += p.getMonto();
        }
        for (Map.Entry<String, double[]> entry
                : metodos.entrySet()) {
            modelMet.addRow(new Object[]{
                entry.getKey(),
                (int) entry.getValue()[0],
                "S/ " + String.format("%.2f",
                        entry.getValue()[1])
            });
        }
        tablaMet.setModel(modelMet);

        JScrollPane scrollMet = new JScrollPane(tablaMet);
        scrollMet.setBounds(16, 342, 392, 150);
        scrollMet.setBorder(new LineBorder(
                new Color(200, 210, 220)));
        add(scrollMet);
    }

    private void agregarTarjeta(int x, int y, String texto,
                                 String numero, Color fondo,
                                 Color colorNum) {
        JPanel card = new JPanel();
        card.setLayout(null);
        card.setBackground(fondo);
        card.setBounds(x, y, 88, 62);
        card.setBorder(new LineBorder(new Color(200, 210, 220)));

        JLabel lblNum = new JLabel(numero);
        lblNum.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNum.setForeground(colorNum);
        lblNum.setBounds(20, 8, 68, 28);
        card.add(lblNum);

        JLabel lblTxt = new JLabel(texto);
        lblTxt.setFont(new Font("Tahoma", Font.PLAIN, 9));
        lblTxt.setForeground(Color.GRAY);
        lblTxt.setBounds(6, 38, 80, 14);
        card.add(lblTxt);

        add(card);
    }
}