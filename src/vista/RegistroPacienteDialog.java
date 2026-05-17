package vista;

import controlador.PacienteControlador;
import modelo.Paciente;
import modelo.Persona;
import com.toedter.calendar.JDateChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class RegistroPacienteDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtDni;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JDateChooser dateFechaNac;
    private JLabel lblEdadCalc;
    private JRadioButton rbMasculino;
    private JRadioButton rbFemenino;
    private JComboBox<String> cmbSeguro;
    
    // Para edición
    private int pacienteId = -1;
    private boolean modoEdicion = false;

    public RegistroPacienteDialog(Frame parent) {
        super(parent, "Registro de Paciente", true);
        construir();
    }
    
    public RegistroPacienteDialog(Frame parent, Paciente p) {
        super(parent, "Editar Paciente", true);
        this.pacienteId = p.getId();
        this.modoEdicion = true;
        construir();
        txtNombre.setText(p.getNombre());
        txtApellido.setText(p.getApellido());
        txtDni.setText(p.getDni());
        txtTelefono.setText(p.getTelefono());
        txtCorreo.setText(p.getCorreo());
        if (p.getSexo().equals("Femenino")) {
            rbFemenino.setSelected(true);
        } else {
            rbMasculino.setSelected(true);
        }
        cmbSeguro.setSelectedItem(p.getTipoSeguro());
        try {
            java.util.Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(p.getFechaNacimiento());
            dateFechaNac.setDate(fecha);
            lblEdadCalc.setText(String.valueOf(Persona.calcularEdad(p.getFechaNacimiento())) + " años");
        } catch (Exception e) {
            lblEdadCalc.setText("—");
        }
    }
    
    private void construir() {
        setSize(440, 490);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // ── TÍTULO ───────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel(
                modoEdicion ? "EDITAR PACIENTE"
                        : "REGISTRO DE PACIENTE");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(110, 14, 240, 22);
        getContentPane().add(lblTitulo);

        // ── NOMBRE ───────────────────────────────────────────────────
        JLabel lblNombre = new JLabel("Nombre: *");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNombre.setBounds(20, 50, 80, 20);
        getContentPane().add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(110, 50, 300, 22);
        getContentPane().add(txtNombre);

        // ── APELLIDO ─────────────────────────────────────────────────
        JLabel lblApellido = new JLabel("Apellido: *");
        lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblApellido.setBounds(20, 82, 80, 20);
        getContentPane().add(lblApellido);
        txtApellido = new JTextField();
        txtApellido.setBounds(110, 82, 300, 22);
        getContentPane().add(txtApellido);

        // ── DNI ──────────────────────────────────────────────────────
        JLabel lblDni = new JLabel("DNI: *");
        lblDni.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDni.setBounds(20, 114, 80, 20);
        getContentPane().add(lblDni);
        txtDni = new JTextField();
        txtDni.setBounds(110, 114, 300, 22);
        getContentPane().add(txtDni);

        // ── FECHA NACIMIENTO (la edad se calcula automáticamente) ────
        JLabel lblFecha = new JLabel("F. Nacimiento: *");
        lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFecha.setBounds(20, 146, 100, 20);
        getContentPane().add(lblFecha);
        dateFechaNac = new JDateChooser();
        dateFechaNac.setDateFormatString("yyyy-MM-dd");
        dateFechaNac.setBounds(110, 146, 200, 22);
        dateFechaNac.getDateEditor().addPropertyChangeListener(
                "date", evt -> {
                    if (dateFechaNac.getDate() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String fecha = sdf.format(dateFechaNac.getDate());
                        int edad = Persona.calcularEdad(fecha);
                        lblEdadCalc.setText(String.valueOf(edad) + " años");
                    } else {
                        lblEdadCalc.setText("—");
                    }
                }
        );
        getContentPane().add(dateFechaNac);

        // ── EDAD CALCULADA (informativo, no se edita) ────────────────
        lblEdadCalc = new JLabel("—");
        lblEdadCalc.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblEdadCalc.setForeground(new Color(26, 95, 168));
        lblEdadCalc.setBounds(320, 146, 90, 22);
        getContentPane().add(lblEdadCalc);

        // ── SEXO ─────────────────────────────────────────────────────
        JLabel lblSexo = new JLabel("Sexo: *");
        lblSexo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSexo.setBounds(20, 178, 80, 20);
        getContentPane().add(lblSexo);
        rbMasculino = new JRadioButton("Masculino");
        rbMasculino.setFont(new Font("Tahoma", Font.PLAIN, 11));
        rbMasculino.setBackground(Color.WHITE);
        rbMasculino.setBounds(110, 178, 90, 20);
        rbMasculino.setSelected(true);
        getContentPane().add(rbMasculino);
        rbFemenino = new JRadioButton("Femenino");
        rbFemenino.setFont(new Font("Tahoma", Font.PLAIN, 11));
        rbFemenino.setBackground(Color.WHITE);
        rbFemenino.setBounds(210, 178, 90, 20);
        getContentPane().add(rbFemenino);
        ButtonGroup grp = new ButtonGroup();
        grp.add(rbMasculino);
        grp.add(rbFemenino);

        // ── TELÉFONO ─────────────────────────────────────────────────
        JLabel lblTel = new JLabel("Teléfono: *");
        lblTel.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblTel.setBounds(20, 210, 80, 20);
        getContentPane().add(lblTel);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(110, 210, 300, 22);
        getContentPane().add(txtTelefono);

        // ── CORREO ───────────────────────────────────────────────────
        JLabel lblCorreo = new JLabel("Correo: *");
        lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblCorreo.setBounds(20, 242, 80, 20);
        getContentPane().add(lblCorreo);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(110, 242, 300, 22);
        getContentPane().add(txtCorreo);

        // ── TIPO SEGURO ──────────────────────────────────────────────
        JLabel lblSeguro = new JLabel("Tipo Seguro: *");
        lblSeguro.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSeguro.setBounds(20, 274, 90, 20);
        getContentPane().add(lblSeguro);
        cmbSeguro = new JComboBox<>(new String[]{
            "SIS", "EsSalud", "Particular", "SOAT"});
        cmbSeguro.setBounds(110, 274, 300, 22);
        getContentPane().add(cmbSeguro);
        
        // ── NOTA CAMPOS OBLIGATORIOS ─────────────────────────────────
        JLabel lblNota = new JLabel("* Campos obligatorios  |  La edad se calcula automáticamente");
        lblNota.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(20, 302, 350, 16);
        getContentPane().add(lblNota);

        // ── BOTÓN REGISTRAR ──────────────────────────────────────────
        JButton btnRegistrar = new JButton(
                modoEdicion ? "Guardar cambios"
                        : "Registrar Paciente");
        btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(26, 95, 168));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBounds(110, 330, 170, 30);
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
                if (dateFechaNac.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione la fecha de nacimiento.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
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

                String sexo = rbMasculino.isSelected()
                        ? "Masculino" : "Femenino";
                SimpleDateFormat sdf =
                        new SimpleDateFormat("yyyy-MM-dd");
                String fechaNac = sdf.format(
                        dateFechaNac.getDate());

                String resultado;
                if (modoEdicion) {
                    resultado = PacienteControlador.actualizar(
                            pacienteId,
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtDni.getText().trim(),
                            txtTelefono.getText().trim(),
                            txtCorreo.getText().trim(),
                            fechaNac, sexo,
                            (String) cmbSeguro.getSelectedItem());
                } else {
                    resultado = PacienteControlador.registrar(
                            txtNombre.getText().trim(),
                            txtApellido.getText().trim(),
                            txtDni.getText().trim(),
                            txtTelefono.getText().trim(),
                            txtCorreo.getText().trim(),
                            fechaNac, sexo,
                            (String) cmbSeguro.getSelectedItem());
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

        // ── BOTÓN CANCELAR ───────────────────────────────────────────
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnCancelar.setBounds(290, 330, 100, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}
