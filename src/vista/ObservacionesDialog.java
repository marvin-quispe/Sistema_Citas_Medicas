package vista;

import controlador.CitaControlador;
import modelo.Cita;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ObservacionesDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextArea txtObservaciones;

    public ObservacionesDialog(Frame parent, int citaId) {
        super(parent, "Registrar Observaciones", true);
        setSize(400, 280);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        Cita cita = CitaControlador.buscarPorId(citaId);

        JLabel lblTitulo = new JLabel(
                "Observaciones — Cita #" + citaId);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(60, 14, 300, 22);
        getContentPane().add(lblTitulo);

        if (cita != null) {
            JLabel lblPac = new JLabel(
                    "Paciente: "
                    + cita.getPaciente().getNombreCompleto());
            lblPac.setFont(new Font("Tahoma", Font.PLAIN, 11));
            lblPac.setForeground(Color.GRAY);
            lblPac.setBounds(20, 38, 360, 16);
            getContentPane().add(lblPac);
        }

        JLabel lblObs = new JLabel("Observaciones clínicas:");
        lblObs.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblObs.setBounds(20, 62, 160, 20);
        getContentPane().add(lblObs);

        txtObservaciones = new JTextArea();
        txtObservaciones.setFont(new Font("Tahoma", Font.PLAIN, 11));
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        if (cita != null && !cita.getObservaciones().isEmpty()) {
            txtObservaciones.setText(cita.getObservaciones());
        }

        JScrollPane scroll = new JScrollPane(txtObservaciones);
        scroll.setBounds(20, 82, 350, 100);
        getContentPane().add(scroll);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGuardar.setBackground(new Color(26, 95, 168));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setOpaque(true);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setBounds(120, 196, 120, 30);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String resultado =
                        CitaControlador.registrarObservaciones(
                                citaId,
                                txtObservaciones.getText().trim());
                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Observación guardada",
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
        getContentPane().add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnCancelar.setBounds(250, 196, 100, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}