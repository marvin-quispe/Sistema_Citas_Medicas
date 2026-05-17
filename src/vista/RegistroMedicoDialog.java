package vista;

import controlador.MedicoControlador;
import datos.ListaEspecialidades;
import modelo.Especialidad;
import modelo.Medico;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RegistroMedicoDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtCmp;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JComboBox<String> cmbTurno;
    private JComboBox<Especialidad> cmbEspecialidad;

    private int medicoId = -1;
    private boolean modoEdicion = false;

    public RegistroMedicoDialog(Frame parent) {
        super(parent, "Registro de Médico", true);
        construir();
    }

    /**
     * @wbp.parser.constructor
     */
    public RegistroMedicoDialog(Frame parent, Medico m) {
        super(parent, "Editar Médico", true);
        this.medicoId = m.getId();
        this.modoEdicion = true;
        construir();

        txtNombre.setText(m.getNombre());
        txtApellido.setText(m.getApellido());
        txtDni.setText(m.getDni());
        txtCmp.setText(m.getCmp());
        txtTelefono.setText(m.getTelefono());
        txtCorreo.setText(m.getCorreo());

        for (int i = 0; i < cmbTurno.getItemCount(); i++) {
            String turnoCombo = cmbTurno.getItemAt(i);
            if (turnoCombo.startsWith(m.getTurno())) {
                cmbTurno.setSelectedIndex(i);
                break;
            }
        }

        Especialidad espMedico = m.getEspecialidad();
        if (espMedico != null) {
            for (int i = 0; i < cmbEspecialidad.getItemCount(); i++) {
                if (cmbEspecialidad.getItemAt(i).getId() == espMedico.getId()) {
                    cmbEspecialidad.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void construir() {
        setSize(440, 460);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(
                modoEdicion ? "EDITAR MÉDICO"
                        : "REGISTRO DE MÉDICO");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(130, 14, 200, 22);
        getContentPane().add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre: *");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNombre.setBounds(20, 50, 80, 20);
        getContentPane().add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(110, 50, 300, 22);
        getContentPane().add(txtNombre);

        JLabel lblApellido = new JLabel("Apellido: *");
        lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblApellido.setBounds(20, 82, 80, 20);
        getContentPane().add(lblApellido);
        txtApellido = new JTextField();
        txtApellido.setBounds(110, 82, 300, 22);
        getContentPane().add(txtApellido);

        JLabel lblDni = new JLabel("DNI: *");
        lblDni.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDni.setBounds(20, 114, 80, 20);
        getContentPane().add(lblDni);
        txtDni = new JTextField();
        txtDni.setBounds(110, 114, 300, 22);
        getContentPane().add(txtDni);

        JLabel lblCmp = new JLabel("CMP: *");
        lblCmp.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblCmp.setBounds(20, 146, 80, 20);
        getContentPane().add(lblCmp);
        txtCmp = new JTextField();
        txtCmp.setBounds(110, 146, 300, 22);
        getContentPane().add(txtCmp);

        JLabel lblEsp = new JLabel("Especialidad: *");
        lblEsp.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEsp.setBounds(20, 178, 90, 20);
        getContentPane().add(lblEsp);
        cmbEspecialidad = new JComboBox<>();
        ArrayList<Especialidad> esps =
                ListaEspecialidades.obtenerTodas();
        for (Especialidad esp : esps) {
            cmbEspecialidad.addItem(esp);
        }
        cmbEspecialidad.setBounds(110, 178, 300, 22);
        getContentPane().add(cmbEspecialidad);

        JLabel lblTurno = new JLabel("Turno: *");
        lblTurno.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblTurno.setBounds(20, 210, 80, 20);
        getContentPane().add(lblTurno);
        cmbTurno = new JComboBox<>(new String[]{
            "Mañana (7am-1pm)",
            "Tarde (1pm-7pm)",
            "Noche (7pm-7am)"});
        cmbTurno.setBounds(110, 210, 300, 22);
        getContentPane().add(cmbTurno);

        JLabel lblTel = new JLabel("Teléfono: *");
        lblTel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblTel.setBounds(20, 242, 80, 20);
        getContentPane().add(lblTel);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(110, 242, 300, 22);
        getContentPane().add(txtTelefono);

        JLabel lblCorreo = new JLabel("Correo: *");
        lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblCorreo.setBounds(20, 274, 80, 20);
        getContentPane().add(lblCorreo);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(110, 274, 300, 22);
        getContentPane().add(txtCorreo);

        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(20, 302, 160, 16);
        getContentPane().add(lblNota);

        JButton btnRegistrar = new JButton(
                modoEdicion ? "Guardar cambios"
                        : "Registrar Médico");
        btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(26, 95, 168));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBounds(110, 334, 160, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // ── VALIDACIONES ─────────────────────────────────────
                if (txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El nombre es obligatorio.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtNombre.requestFocus();
                    return;
                }
                if (txtApellido.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El apellido es obligatorio.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtApellido.requestFocus();
                    return;
                }
                if (txtDni.getText().trim().isEmpty()
                        || txtDni.getText().trim().length() != 8) {
                    JOptionPane.showMessageDialog(null,
                            "El DNI debe tener 8 dígitos.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtDni.requestFocus();
                    return;
                }
                if (txtCmp.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El CMP es obligatorio.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtCmp.requestFocus();
                    return;
                }
                if (txtTelefono.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El teléfono es obligatorio.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtTelefono.requestFocus();
                    return;
                }
                if (txtCorreo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "El correo es obligatorio.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    txtCorreo.requestFocus();
                    return;
                }
                // ─────────────────────────────────────────────────────

                Especialidad espSel =
                        (Especialidad) cmbEspecialidad
                        .getSelectedItem();
                String resultado;
                if (modoEdicion) {
                    resultado = MedicoControlador.actualizar(
                            medicoId,
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtDni.getText().trim(),
                            txtCmp.getText().trim(),
                            txtTelefono.getText().trim(),
                            txtCorreo.getText().trim(),
                            (String) cmbTurno.getSelectedItem(),
                            espSel);
                } else {
                    resultado = MedicoControlador.registrar(
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtDni.getText().trim(),
                            txtCmp.getText().trim(),
                            txtTelefono.getText().trim(),
                            txtCorreo.getText().trim(),
                            (String) cmbTurno.getSelectedItem(),
                            espSel);
                }

                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            modoEdicion ? "Actualizado"
                                    : "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("ERROR: ", ""),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        getContentPane().add(btnRegistrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnCancelar.setBounds(280, 334, 100, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}