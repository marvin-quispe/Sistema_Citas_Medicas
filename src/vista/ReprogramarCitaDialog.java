package vista;

import controlador.CitaControlador;
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

public class ReprogramarCitaDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JTextField txtFecha;
    private JComboBox<String> cmbHora;

    public ReprogramarCitaDialog(Frame parent, int citaId) {
        super(parent, "Reprogramar Cita", true);
        setSize(380, 240);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("REPROGRAMAR CITA #" + citaId);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTitulo.setForeground(new Color(230, 126, 34));
        lblTitulo.setBounds(60, 14, 270, 22);
        getContentPane().add(lblTitulo);

        JLabel lblFecha = new JLabel("Nueva fecha:");
        lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFecha.setBounds(20, 54, 90, 20);
        getContentPane().add(lblFecha);
        txtFecha = new JTextField();
        txtFecha.setToolTipText("Formato: yyyy-MM-dd");
        txtFecha.setBounds(120, 54, 220, 22);
        getContentPane().add(txtFecha);

        JLabel lblHora = new JLabel("Nueva hora:");
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblHora.setBounds(20, 86, 90, 20);
        getContentPane().add(lblHora);
        cmbHora = new JComboBox<>(new String[]{
            "08:00", "08:30", "09:00", "09:30",
            "10:00", "10:30", "11:00", "11:30",
            "14:00", "14:30", "15:00", "16:00"});
        cmbHora.setBounds(120, 86, 220, 22);
        getContentPane().add(cmbHora);

        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnConfirmar.setBackground(new Color(230, 126, 34));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setOpaque(true);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setBounds(120, 136, 120, 30);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String resultado = CitaControlador.reprogramar(
                        citaId,
                        txtFecha.getText().trim(),
                        (String) cmbHora.getSelectedItem());
                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Cita reprogramada",
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
        getContentPane().add(btnConfirmar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnCancelar.setBounds(250, 136, 90, 30);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getContentPane().add(btnCancelar);
    }
}