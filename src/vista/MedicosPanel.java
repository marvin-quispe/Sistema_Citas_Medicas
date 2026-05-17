package vista;

import controlador.MedicoControlador;
import modelo.Medico;
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

public class MedicosPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaMedicos;

    public MedicosPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Registro de Médicos");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel(
                "Gestión de médicos — RF-09 a RF-14");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 300, 16);
        add(lblSub);

        JButton btnNuevo = new JButton("+ Nuevo Médico");
        btnNuevo.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnNuevo.setBackground(new Color(26, 95, 168));
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setOpaque(true);
        btnNuevo.setBorderPainted(false);
        btnNuevo.setBounds(460, 60, 90, 26);
        btnNuevo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegistroMedicoDialog dialog =
                        new RegistroMedicoDialog(null);
                dialog.setVisible(true);
                cargarTabla();
            }
        });
        add(btnNuevo);

        tablaMedicos = new JTable();
        tablaMedicos.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaMedicos.setRowHeight(22);
        tablaMedicos.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablaMedicos.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));

        JScrollPane scroll = new JScrollPane(tablaMedicos);
        scroll.setBounds(16, 96, 392, 400);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnEditar.setBackground(new Color(39, 174, 96));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setOpaque(true);
        btnEditar.setBorderPainted(false);
        btnEditar.setBounds(16, 564, 100, 26);
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila = tablaMedicos.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un médico.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaMedicos.getModel()
                        .getValueAt(fila, 0);
                Medico m = MedicoControlador.buscarPorId(id);
                if (m == null) return;
                RegistroMedicoDialog dialog =
                        new RegistroMedicoDialog(null, m);
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
                int fila = tablaMedicos.getSelectedRow();
                if (fila == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un médico de la tabla.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                int id = (int) tablaMedicos.getModel()
                        .getValueAt(fila, 0);
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "¿Está seguro de eliminar este médico?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                String res = MedicoControlador.eliminar(id);
                if (res.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            res.replace("OK: ", ""),
                            "Éxito",
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
        add(btnEliminar);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] columnas = {"ID", "Nombre", "CMP",
                "Especialidad", "Turno", "Disponible"};
        DefaultTableModel model =
                new DefaultTableModel(columnas, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Medico> lista = MedicoControlador.listar();
        for (Medico m : lista) {
            model.addRow(new Object[]{
                m.getId(),
                m.getNombreCompleto(),
                m.getCmp(),
                m.getEspecialidad() != null
                        ? m.getEspecialidad().getNombre() : "—",
                m.getTurno(),
                m.isDisponible() ? "Sí" : "No"
            });
        }
        tablaMedicos.setModel(model);
        tablaMedicos.getColumnModel()
                .getColumn(0).setMaxWidth(40);
    }
}