package vista;

import controlador.MedicoControlador;
import controlador.UrgenciaControlador;
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

public class RegistroUrgenciaDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtNombre;
    private JTextField txtDni;
    private JTextField txtMotivo;
    private JComboBox<String> cmbPrioridad;
    private JComboBox<Medico> cmbMedico;

    public RegistroUrgenciaDialog(Frame parent) {
        super(parent, "Atención de Urgencia", true);
        setSize(420, 360);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("REGISTRO DE URGENCIA");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(163, 45, 45));
        lblTitulo.setBounds(110, 14, 220, 22);
        getContentPane().add(lblTitulo);

        JLabel lblAviso = new JLabel(
                "Atención inmediata — sin cita previa");
        lblAviso.setFont(new Font("Tahoma", Font.ITALIC, 10));
        lblAviso.setForeground(Color.GRAY);
        lblAviso.setBounds(100, 36, 230, 16);
        getContentPane().add(lblAviso);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNombre.setBounds(20, 64, 80, 20);
        getContentPane().add(lblNombre);
        txtNombre = new JTextField();
        txtNombre.setBounds(110, 64, 280, 22);
        getContentPane().add(txtNombre);

        JLabel lblDni = new JLabel("DNI:");
        lblDni.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblDni.setBounds(20, 96, 80, 20);
        getContentPane().add(lblDni);
        txtDni = new JTextField();
        txtDni.setBounds(110, 96, 280, 22);
        getContentPane().add(txtDni);

        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMotivo.setBounds(20, 128, 80, 20);
        getContentPane().add(lblMotivo);
        txtMotivo = new JTextField();
        txtMotivo.setBounds(110, 128, 280, 22);
        getContentPane().add(txtMotivo);

        JLabel lblPrior = new JLabel("Prioridad:");
        lblPrior.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPrior.setBounds(20, 160, 80, 20);
        getContentPane().add(lblPrior);
        cmbPrioridad = new JComboBox<>(new String[]{
            "Alta — Riesgo de vida",
            "Media — Urgente pero estable",
            "Baja — Puede esperar"});
        cmbPrioridad.setBounds(110, 160, 280, 22);
        getContentPane().add(cmbPrioridad);

        JLabel lblMed = new JLabel("Doctor:");
        lblMed.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMed.setBounds(20, 192, 80, 20);
        getContentPane().add(lblMed);
        cmbMedico = new JComboBox<>();
        ArrayList<Medico> meds =
                MedicoControlador.listarDisponibles();
        for (Medico m : meds) {
            cmbMedico.addItem(m);
        }
        cmbMedico.setBounds(110, 192, 280, 22);
        getContentPane().add(cmbMedico);

        JButton btnRegistrar = new JButton("Enviar a Urgencias");
        btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(163, 45, 45));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBounds(110, 240, 170, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Medico med =
                        (Medico) cmbMedico.getSelectedItem();
                String prior = ((String) cmbPrioridad
                        .getSelectedItem()).split(" — ")[0];
                String resultado = UrgenciaControlador.registrar(
                        txtNombre.getText().trim(),
                        txtDni.getText().trim(),
                        txtMotivo.getText().trim(),
                        prior, med);
                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Urgencia registrada",
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
        btnCancelar.setBounds(290, 240, 100, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}