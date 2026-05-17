package vista;

import datos.ListaEspecialidades;
import modelo.Especialidad;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
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

public class EspecialidadesPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable tablaEsp;
    private JTextField txtNombre;
    private JTextField txtDesc;

    public EspecialidadesPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        JLabel lblTitulo = new JLabel("Especialidades Médicas");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 250, 25);
        add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("RF-12 a RF-14");
        lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(16, 34, 200, 16);
        add(lblSubtitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNombre.setBounds(16, 62, 70, 20);
        add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(86, 62, 160, 22);
        add(txtNombre);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDesc.setBounds(16, 92, 70, 20);
        add(lblDesc);
        txtDesc = new JTextField();
        txtDesc.setBounds(86, 92, 220, 22);
        add(txtDesc);

        JButton btnAgregar = new JButton("+ Agregar");
        btnAgregar.setFont(new Font("Tahoma", Font.BOLD, 11));
        btnAgregar.setBackground(new Color(26, 95, 168));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setOpaque(true);
        btnAgregar.setBorderPainted(false);
        btnAgregar.setBounds(316, 62, 90, 52);
        btnAgregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = txtNombre.getText().trim();
                String desc = txtDesc.getText().trim();
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre es obligatorio.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (ListaEspecialidades.existe(nombre)) {
                    JOptionPane.showMessageDialog(null,
                            "Ya existe esa especialidad.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Especialidad nueva = new Especialidad(
                        0, nombre, desc);
                ListaEspecialidades.agregar(nueva);
                txtNombre.setText("");
                txtDesc.setText("");
                cargarTabla();
                JOptionPane.showMessageDialog(null,
                        "Especialidad registrada correctamente.\n"
                        + "Nombre: " + nombre,
                        "Registro exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        add(btnAgregar);

        tablaEsp = new JTable();
        tablaEsp.setFont(new Font("Tahoma", Font.PLAIN, 11));
        tablaEsp.setRowHeight(22);
        tablaEsp.getTableHeader().setDefaultRenderer(
                new HeaderRenderer(new Color(26, 95, 168)));
        tablaEsp.setEnabled(false);

        JScrollPane scroll = new JScrollPane(tablaEsp);
        scroll.setBounds(16, 128, 392, 380);
        scroll.setBorder(new LineBorder(new Color(200, 210, 220)));
        add(scroll);

        cargarTabla();
    }

    private void cargarTabla() {
        String[] cols = {"ID", "Nombre", "Descripción"};
        DefaultTableModel model =
                new DefaultTableModel(cols, 0) {
                    public boolean isCellEditable(
                            int row, int col) {
                        return false;
                    }
                };
        ArrayList<Especialidad> lista =
                ListaEspecialidades.obtenerTodas();
        for (Especialidad e : lista) {
            model.addRow(new Object[]{
                e.getId(), e.getNombre(), e.getDescripcion()
            });
        }
        tablaEsp.setModel(model);
        tablaEsp.getColumnModel().getColumn(0).setMaxWidth(40);
    }
}