package vista;

import controlador.CitaControlador;
import controlador.PacienteControlador;
import modelo.Cita;
import modelo.Paciente;
import modelo.Usuario;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class MisCitasPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaCitas;
    private Usuario usuarioSesion;

    public MisCitasPanel(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Mis Citas Médicas");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        Paciente paciente = PacienteControlador.buscarPorId(usuario.getEntidadId());
        String nombrePaciente = paciente != null ? paciente.getNombreCompleto() : usuario.getNombre();

        JLabel lblSub = new JLabel("Paciente: " + nombrePaciente);
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 350, 16);
        add(lblSub);

        tablaCitas = new JTable();
        tablaCitas.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaCitas.setRowHeight(22);
        tablaCitas.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaCitas);
        scroll.setBounds(16, 62, 680, 400);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] cols = {"ID", "Doctor", "Especialidad", "Fecha", "Hora", "Motivo", "Estado", "Observaciones"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        ArrayList<Cita> lista = CitaControlador.listarPorPaciente(usuarioSesion.getEntidadId());
        for (Cita c : lista) {
            model.addRow(new Object[]{
                c.getId(),
                c.getMedico().getNombreCompleto(),
                c.getEspecialidad() != null ? c.getEspecialidad().getNombre() : "—",
                c.getFecha(),
                c.getHora(),
                c.getMotivo(),
                c.getEstado(),
                c.getObservaciones().isEmpty() ? "Sin observaciones" : c.getObservaciones()
            });
        }
        tablaCitas.setModel(model);
        tablaCitas.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}
