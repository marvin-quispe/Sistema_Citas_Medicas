package vista;

import controlador.Autorizador;
import datos.ListaHorarios;
import controlador.MedicoControlador;
import modelo.Horario;
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

public class HorariosPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaHorarios;
    private JComboBox<Medico> cmbMedico;
    private JComboBox<String> cmbDia;
    private JComboBox<String> cmbTurno;
    private Usuario usuarioSesion;

    public HorariosPanel(Usuario usuario) {
        this.usuarioSesion = usuario;
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        boolean esAdm = Autorizador.esAdmin(usuario);

        JLabel lblTitulo = new JLabel("Horarios Médicos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        int formY = 62;

        // ── SELECTOR DE MÉDICO (visible para todos los roles) ────────
        JLabel lblMed = new JLabel("Médico:");
        lblMed.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMed.setBounds(16, formY, 60, 20);
        add(lblMed);

        cmbMedico = new JComboBox<>();
        ArrayList<Medico> medicos = MedicoControlador.listar();
        for (Medico m : medicos) {
            cmbMedico.addItem(m);
        }
        cmbMedico.setBounds(76, formY, 200, 22);
        cmbMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarTabla();
            }
        });
        add(cmbMedico);

        // ── FORMULARIO SOLO ADMIN ────────────────────────────────────
        if (esAdm) {
            JLabel lblDia = new JLabel("Día:");
            lblDia.setFont(new Font("Tahoma", Font.PLAIN, 11));
            lblDia.setBounds(16, formY + 30, 60, 20);
            add(lblDia);

            cmbDia = new JComboBox<>(new String[]{
                "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"});
            cmbDia.setBounds(76, formY + 30, 160, 22);
            add(cmbDia);

            JLabel lblTurno = new JLabel("Turno:");
            lblTurno.setFont(new Font("Tahoma", Font.PLAIN, 11));
            lblTurno.setBounds(16, formY + 60, 60, 20);
            add(lblTurno);

            cmbTurno = new JComboBox<>(new String[]{"Mañana", "Tarde", "Noche"});
            cmbTurno.setBounds(76, formY + 60, 160, 22);
            add(cmbTurno);

            JButton btnAgregar = new JButton("+ Agregar Horario");
            btnAgregar.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnAgregar.setBackground(new Color(26, 95, 168));
            btnAgregar.setForeground(Color.WHITE);
            btnAgregar.setOpaque(true);
            btnAgregar.setBorderPainted(false);
            btnAgregar.setBounds(284, formY + 30, 150, 26);
            btnAgregar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Medico med = (Medico) cmbMedico.getSelectedItem();
                    String dia = (String) cmbDia.getSelectedItem();
                    String turno = (String) cmbTurno.getSelectedItem();
                    if (med == null) {
                        JOptionPane.showMessageDialog(null, "Seleccione un médico.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (ListaHorarios.existeHorario(med.getId(), dia, turno)) {
                        JOptionPane.showMessageDialog(null, "Ya existe ese horario para " + med.getNombreCompleto() + ".", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    Horario nuevo = new Horario(0, med, dia, turno, true);
                    ListaHorarios.agregar(nuevo);
                    cargarTabla();
                    JOptionPane.showMessageDialog(null, "Horario registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            add(btnAgregar);

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Tahoma", Font.BOLD, 11));
            btnEliminar.setBackground(new Color(192, 57, 43));
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setOpaque(true);
            btnEliminar.setBorderPainted(false);
            btnEliminar.setBounds(284, formY + 60, 150, 26);
            btnEliminar.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int fila = tablaHorarios.getSelectedRow();
                    if (fila == -1) {
                        JOptionPane.showMessageDialog(null, "Seleccione un horario.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    int id = (int) tablaHorarios.getModel().getValueAt(fila, 0);
                    ListaHorarios.eliminar(id);
                    cargarTabla();
                    JOptionPane.showMessageDialog(null, "Horario eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            add(btnEliminar);
        }

        // ── TABLA ────────────────────────────────────────────────────
        tablaHorarios = new JTable();
        tablaHorarios.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaHorarios.setRowHeight(22);
        tablaHorarios.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaHorarios);
        scroll.setBounds(16, esAdm ? formY + 100 : formY + 36, 500, 360);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] cols = {"ID", "Médico", "Día", "Turno", "Disponible"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        Medico sel = (Medico) cmbMedico.getSelectedItem();
        ArrayList<Horario> lista = new ArrayList<>();
        if (sel != null) {
            lista = ListaHorarios.buscarPorMedico(sel.getId());
        } else {
            lista = ListaHorarios.obtenerTodos();
        }

        for (Horario h : lista) {
            model.addRow(new Object[]{
                h.getId(),
                h.getMedico().getNombreCompleto(),
                h.getDiaSemana(),
                h.getTurno(),
                h.isDisponible() ? "Sí" : "No"
            });
        }
        tablaHorarios.setModel(model);
        tablaHorarios.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}
