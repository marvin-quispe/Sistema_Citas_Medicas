package vista;

import controlador.Autorizador;
import controlador.CitaControlador;
import controlador.MedicoControlador;
import controlador.PacienteControlador;
import controlador.PagoControlador;
import controlador.UrgenciaControlador;
import modelo.Cita;
import modelo.Urgencia;
import modelo.Usuario;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class DashboardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel lblNumPac;
    private JLabel lblNumMed;
    private JLabel lblNumCit;
    private JLabel lblNumPag;
    private JLabel lblNumUrg;
    private JLabel lblNumIng;
    private JTable tableCitas;
    private JTable tableUrgencias;
    private Usuario usuarioSesion;

    public DashboardPanel(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Dashboard");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 200, 25);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Resumen del sistema");
        lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(16, 34, 200, 16);
        add(lblSubtitulo);

        boolean showAll = Autorizador.esAdmin(usuarioSesion) || Autorizador.esRecepcionista(usuarioSesion);

        int[] xs = {16, 93, 170, 247, 324, 401};
        Color[] colores = {
            new Color(232, 241, 251),
            new Color(232, 248, 240),
            new Color(255, 248, 225),
            new Color(232, 241, 251),
            new Color(253, 232, 232),
            new Color(237, 247, 237)
        };
        String[] etiquetas = {"Pacientes", "Médicos", "Citas", "Pend. Pago", "Urgencias", "Ingresos"};
        Color[] coloresNum = {
            new Color(26, 95, 168),
            new Color(15, 110, 86),
            new Color(133, 79, 11),
            new Color(230, 126, 34),
            new Color(163, 45, 45),
            new Color(39, 174, 96)
        };

        JLabel[] labels = {lblNumPac, lblNumMed, lblNumCit, lblNumPag, lblNumUrg, lblNumIng};

        for (int i = 0; i < 6; i++) {
            JPanel card = new JPanel();
            card.setLayout(null);
            card.setBackground(colores[i]);
            card.setBounds(xs[i], 62, 69, 62);
            card.setBorder(new LineBorder(new Color(200, 210, 220)));
            JLabel num = new JLabel("0");
            num.setBounds(4, 8, 61, 28);
            card.add(num);
            num.setFont(new Font("Tahoma", Font.BOLD, 20));
            num.setForeground(coloresNum[i]);
            JLabel lblTexto = new JLabel(etiquetas[i]);
            lblTexto.setBounds(4, 38, 65, 14);
            card.add(lblTexto);
            lblTexto.setFont(new Font("Tahoma", Font.PLAIN, 9));
            lblTexto.setForeground(Color.GRAY);

            switch (i) {
                case 0: lblNumPac = num; if (!showAll) continue; break;
                case 1: lblNumMed = num; if (!showAll) continue; break;
                case 2: lblNumCit = num; break;
                case 3: lblNumPag = num; if (!Autorizador.esAdmin(usuarioSesion) && !Autorizador.esCajero(usuarioSesion)) continue; break;
                case 4: lblNumUrg = num; if (!Autorizador.puedeAcceder(usuarioSesion.getRol(), "urgencias")) continue; break;
                case 5: lblNumIng = num; break;
            }
            add(card);
        }

        JLabel lblSeccionCitas = new JLabel("Citas registradas");
        lblSeccionCitas.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblSeccionCitas.setForeground(new Color(26, 95, 168));
        lblSeccionCitas.setBounds(16, 127, 200, 20);
        add(lblSeccionCitas);

        tableCitas = new JTable();
        tableCitas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tableCitas.setRowHeight(22);
        tableCitas.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tableCitas.setEnabled(false);

        JScrollPane scrollCitas = new JScrollPane(tableCitas);
        scrollCitas.setBounds(16, 158, 470, 150);
        scrollCitas.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scrollCitas);

        JLabel lblSeccionUrgencias = new JLabel("Urgencias activas");
        lblSeccionUrgencias.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblSeccionUrgencias.setForeground(new Color(163, 45, 45));
        lblSeccionUrgencias.setBounds(16, 320, 200, 20);
        if (!Autorizador.puedeAcceder(usuarioSesion.getRol(), "urgencias")) lblSeccionUrgencias.setVisible(false);
        add(lblSeccionUrgencias);

        tableUrgencias = new JTable();
        tableUrgencias.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tableUrgencias.setRowHeight(22);
        tableUrgencias.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(163, 45, 45)));
        tableUrgencias.setEnabled(false);

        JScrollPane scrollUrgencias = new JScrollPane(tableUrgencias);
        scrollUrgencias.setBounds(16, 342, 470, 150);
        scrollUrgencias.setBorder(new LineBorder(new Color(200, 210, 220)));
        if (!Autorizador.puedeAcceder(usuarioSesion.getRol(), "urgencias")) scrollUrgencias.setVisible(false);
        add(scrollUrgencias);

        cargarDatos();
    }



    public void cargarDatos() {
        boolean showAll = Autorizador.esAdmin(usuarioSesion) || Autorizador.esRecepcionista(usuarioSesion);

        if (showAll) {
            lblNumPac.setText(String.valueOf(PacienteControlador.totalPacientes()));
            lblNumMed.setText(String.valueOf(MedicoControlador.totalMedicos()));
        }

        lblNumCit.setText(String.valueOf(CitaControlador.totalCitas()));

        if (Autorizador.puedeAcceder(usuarioSesion.getRol(), "urgencias")) {
            lblNumUrg.setText(String.valueOf(UrgenciaControlador.totalActivas()));
        }

        if (Autorizador.puedeAcceder(usuarioSesion.getRol(), "pagos") || Autorizador.esAdmin(usuarioSesion)) {
            lblNumIng.setText(String.format("%.0f", PagoControlador.totalIngresos()));
        }

        // Contar citas pendientes de pago (todavía no pagadas)
        int pendientesPago = 0;
        ArrayList<Cita> pendientes = CitaControlador.listarPorEstado("Pendiente");
        for (Cita c : pendientes) {
            if (!PagoControlador.citaYaPagada(c.getId())) {
                pendientesPago++;
            }
        }
        if (lblNumPag != null) lblNumPag.setText(String.valueOf(pendientesPago));

        String[] colCitas = {"Paciente", "Doctor", "Hora", "Motivo", "Estado"};
        DefaultTableModel modelCitas = new DefaultTableModel(colCitas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ArrayList<Cita> citas;
        if (Autorizador.esMedico(usuarioSesion) && usuarioSesion.tieneEnlaceEntidad()) {
            citas = CitaControlador.listarPorMedico(usuarioSesion.getEntidadId());
        } else {
            citas = CitaControlador.listarTodas();
        }

        for (Cita c : citas) {
            modelCitas.addRow(new Object[]{
                c.getPaciente().getNombreCompleto(),
                c.getMedico().getNombreCompleto(),
                c.getHora(),
                c.getMotivo(),
                c.getEstado()
            });
        }
        tableCitas.setModel(modelCitas);

        if (Autorizador.puedeAcceder(usuarioSesion.getRol(), "urgencias")) {
            String[] colUrg = {"Paciente", "Motivo", "Prioridad", "Doctor", "Estado"};
            DefaultTableModel modelUrg = new DefaultTableModel(colUrg, 0) {
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            ArrayList<Urgencia> urgencias = UrgenciaControlador.listarActivas();
            for (Urgencia u : urgencias) {
                modelUrg.addRow(new Object[]{
                    u.getNombre(),
                    u.getMotivo(),
                    u.getPrioridad(),
                    u.getMedico().getNombreCompleto(),
                    u.getEstado()
                });
            }
            tableUrgencias.setModel(modelUrg);
        }
    }
}
