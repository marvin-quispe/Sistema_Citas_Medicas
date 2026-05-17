package vista;

import controlador.CitaControlador;
import controlador.PagoControlador;
import modelo.Cita;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class RegistroPagoDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtMonto;
    private JComboBox<String> cmbMetodo;
    private Cita citaSeleccionada;

    public RegistroPagoDialog(Frame parent, Cita cita) {
        super(parent, "Registrar Pago", true);
        this.citaSeleccionada = cita;
        setSize(440, 320);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        // Fecha automática del sistema
        String fechaHoy = LocalDate.now().toString();
        
        // ── TÍTULO ───────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("REGISTRO DE PAGO");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(130, 14, 200, 22);
        getContentPane().add(lblTitulo);

        // ── DATOS DE LA CITA ─────────────────────────────────────────
        JLabel lblPac = new JLabel("Paciente: " + cita.getPaciente().getNombreCompleto());
        lblPac.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPac.setBounds(20, 48, 400, 20);
        getContentPane().add(lblPac);

        JLabel lblDoc = new JLabel("Doctor: " + cita.getMedico().getNombreCompleto());
        lblDoc.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDoc.setBounds(20, 68, 400, 20);
        getContentPane().add(lblDoc);

        JLabel lblEsp = new JLabel("Especialidad: " + (cita.getEspecialidad() != null ? cita.getEspecialidad().getNombre() : "—"));
        lblEsp.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEsp.setBounds(20, 88, 400, 20);
        getContentPane().add(lblEsp);

        JLabel lblFechaHora = new JLabel("Fecha: " + cita.getFecha() + "  —  Hora: " + cita.getHora());
        lblFechaHora.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFechaHora.setForeground(Color.GRAY);
        lblFechaHora.setBounds(20, 108, 400, 16);
        getContentPane().add(lblFechaHora);
        
        // ── MONTO ────────────────────────────────────────────────────
        JLabel lblMonto = new JLabel("Monto (S/): *");
        lblMonto.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMonto.setBounds(20, 136, 100, 20);
        getContentPane().add(lblMonto);

        txtMonto = new JTextField();
        txtMonto.setBounds(120, 136, 290, 22);
        getContentPane().add(txtMonto);
        
        // ── MÉTODO DE PAGO ───────────────────────────────────────────
        JLabel lblMet = new JLabel("Método: *");
        lblMet.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMet.setBounds(20, 168, 100, 20);
        getContentPane().add(lblMet);

        cmbMetodo = new JComboBox<>(new String[]{
            "Efectivo", "Tarjeta",
            "EsSalud", "SIS", "SOAT"});
        cmbMetodo.setBounds(120, 168, 290, 22);
        getContentPane().add(cmbMetodo);
        
        // ── NOTA ─────────────────────────────────────────────────────
        JLabel lblNota = new JLabel("* Campos obligatorios");
        lblNota.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblNota.setForeground(Color.GRAY);
        lblNota.setBounds(20, 198, 160, 16);
        getContentPane().add(lblNota);

        // ── BOTÓN REGISTRAR ──────────────────────────────────────────
        JButton btnRegistrar = new JButton("Confirmar Pago");
        btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(26, 95, 168));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBounds(120, 228, 150, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ── VALIDACIONES ─────────────────────────────────────
                String montoStr = txtMonto.getText().trim();
                if (montoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Ingrese el monto.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                double monto;
                try {
                    monto = Double.parseDouble(montoStr);
                    if (monto <= 0) throw new Exception();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Ingrese un monto válido mayor a cero.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // ─────────────────────────────────────────────────────

                String resultado = PagoControlador.registrar(
                        cita.getId(),
                        monto,
                        (String) cmbMetodo.getSelectedItem(),
                        fechaHoy);

                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Pago registrado",
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
        btnCancelar.setBounds(280, 228, 100, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}
