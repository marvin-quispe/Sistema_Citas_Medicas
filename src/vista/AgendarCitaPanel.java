package vista;

import controlador.CitaControlador;
import controlador.MedicoControlador;
import controlador.PacienteControlador;
import datos.ListaEspecialidades;
import modelo.Especialidad;
import modelo.Medico;
import modelo.Paciente;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;

public class AgendarCitaPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComboBox<Paciente> cmbPaciente;
    private JComboBox<Especialidad> cmbEspecialidad;
    private JComboBox<Medico> cmbMedico;
    private JDateChooser dateFecha;
    private JComboBox<String> cmbHora;
    private JComboBox<String> cmbMotivo;

    public AgendarCitaPanel() {
        setLayout(null);
        setBackground(new Color(240, 244, 248));

        // ── TÍTULO ───────────────────────────────────────────────────
        JLabel lblTitulo = new JLabel("Agendar Cita Médica");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(26, 95, 168));
        lblTitulo.setBounds(16, 11, 280, 25);
        add(lblTitulo);

        JLabel lblSub = new JLabel(
                "Seleccione especialidad, médico, fecha y hora");
        lblSub.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblSub.setForeground(Color.GRAY);
        lblSub.setBounds(16, 34, 350, 16);
        add(lblSub);

        // ── PANEL FORMULARIO ─────────────────────────────────────────
        JPanel pnlForm = new JPanel();
        pnlForm.setLayout(null);
        pnlForm.setBackground(Color.WHITE);
        pnlForm.setBorder(new LineBorder(
                new Color(200, 210, 220)));
        pnlForm.setBounds(16, 60, 660, 440);
        add(pnlForm);

        JLabel lblFormTitulo = new JLabel("Nueva Cita Médica");
        lblFormTitulo.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblFormTitulo.setForeground(new Color(26, 95, 168));
        lblFormTitulo.setBounds(14, 14, 300, 20);
        pnlForm.add(lblFormTitulo);

        // ── PACIENTE ─────────────────────────────────────────────────
        JLabel lblPac = new JLabel("Paciente:");
        lblPac.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblPac.setBounds(14, 46, 100, 20);
        pnlForm.add(lblPac);

        cmbPaciente = new JComboBox<>();
        ArrayList<Paciente> pacs = PacienteControlador.listar();
        for (Paciente p : pacs) {
            cmbPaciente.addItem(p);
        }
        cmbPaciente.setBounds(14, 66, 630, 22);
        pnlForm.add(cmbPaciente);

        // ── ESPECIALIDAD (seleccionar primero) ───────────────────────
        JLabel lblEsp = new JLabel("Especialidad:");
        lblEsp.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblEsp.setBounds(14, 98, 100, 20);
        pnlForm.add(lblEsp);

        cmbEspecialidad = new JComboBox<>();
        cmbEspecialidad.addItem(null);
        ArrayList<Especialidad> esps = ListaEspecialidades.obtenerTodas();
        for (Especialidad esp : esps) {
            cmbEspecialidad.addItem(esp);
        }
        cmbEspecialidad.setBounds(14, 118, 630, 22);
        cmbEspecialidad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Filtrar médicos por especialidad seleccionada
                cmbMedico.removeAllItems();
                Especialidad espSel = (Especialidad) cmbEspecialidad.getSelectedItem();
                if (espSel != null) {
                    ArrayList<Medico> meds = MedicoControlador.listar();
                    for (Medico m : meds) {
                        if (m.getEspecialidad() != null && m.getEspecialidad().getId() == espSel.getId()) {
                            cmbMedico.addItem(m);
                        }
                    }
                } else {
                    // Si no hay especialidad, mostrar todos
                    ArrayList<Medico> meds = MedicoControlador.listar();
                    for (Medico m : meds) {
                        cmbMedico.addItem(m);
                    }
                }
            }
        });
        pnlForm.add(cmbEspecialidad);

        // ── MÉDICO (filtrado por especialidad) ───────────────────────
        JLabel lblMed = new JLabel("Doctor:");
        lblMed.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMed.setBounds(14, 150, 100, 20);
        pnlForm.add(lblMed);

        cmbMedico = new JComboBox<>();
        cmbMedico.setBounds(14, 170, 630, 22);
        pnlForm.add(cmbMedico);

      // ── FECHA CON CALENDARIO ─────────────────────────────────────
        JLabel lblFecha = new JLabel("Fecha: *");
        lblFecha.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblFecha.setBounds(14, 204, 100, 20);
        pnlForm.add(lblFecha);

        dateFecha = new JDateChooser();
        dateFecha.setDateFormatString("yyyy-MM-dd");
        dateFecha.setBounds(14, 224, 300, 24);
        pnlForm.add(dateFecha);

        // ── HORA ─────────────────────────────────────────────────────
        JLabel lblHora = new JLabel("Hora: *");
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblHora.setBounds(324, 204, 100, 20);
        pnlForm.add(lblHora);

        cmbHora = new JComboBox<>(new String[]{
            "08:00", "08:30", "09:00", "09:30",
            "10:00", "10:30", "11:00", "11:30",
            "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30"});
        cmbHora.setBounds(324, 224, 320, 24);
        pnlForm.add(cmbHora);
        
      // ── MOTIVO ───────────────────────────────────────────────────
        JLabel lblMotivo = new JLabel("Motivo de consulta: *");
        lblMotivo.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblMotivo.setBounds(14, 258, 160, 20);
        pnlForm.add(lblMotivo);

        cmbMotivo = new JComboBox<>(new String[]{
            "Control general",
            "Consulta de seguimiento",
            "Dolor o malestar",
            "Revisión de exámenes",
            "Vacunación",
            "Otro"});
        cmbMotivo.setEditable(true);
        cmbMotivo.setBounds(14, 278, 630, 24);
        pnlForm.add(cmbMotivo);
        
        
        // ── BOTONES ──────────────────────────────────────────────────
        JButton btnRegistrar = new JButton("Registrar Cita");
        btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(26, 95, 168));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setBounds(14, 320, 160, 30);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	// ── VALIDACIONES ─────────────────────────────────────
                if (cmbPaciente.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un paciente.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (cmbMedico.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un doctor.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (dateFecha.getDate() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione la fecha de la cita.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String motivo = cmbMotivo.getEditor()
                        .getItem().toString().trim();
                if (motivo.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Ingrese el motivo de consulta.",
                            "Campo requerido",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // ─────────────────────────────────────────────────────

                Paciente pac =
                        (Paciente) cmbPaciente.getSelectedItem();
                Medico med =
                        (Medico) cmbMedico.getSelectedItem();
                Especialidad esp = med != null
                        ? med.getEspecialidad() : null;
                String fecha = new SimpleDateFormat("yyyy-MM-dd")
                        .format(dateFecha.getDate());

                String resultado = CitaControlador.agendar(
                        pac, med, esp, fecha,
                        (String) cmbHora.getSelectedItem(),
                        motivo);

                if (resultado.startsWith("OK")) {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("OK: ", ""),
                            "Cita registrada — Pendiente de pago en Caja",
                            JOptionPane.INFORMATION_MESSAGE);
                    limpiar();
                } else {
                    JOptionPane.showMessageDialog(null,
                            resultado.replace("ERROR: ", ""),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pnlForm.add(btnRegistrar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Tahoma", Font.PLAIN, 11));
        btnLimpiar.setBounds(184, 320, 100, 30);
        btnLimpiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limpiar();
            }
        });
        pnlForm.add(btnLimpiar);
    }

    private void limpiar() {
    	if (cmbPaciente.getItemCount() > 0) {
            cmbPaciente.setSelectedIndex(0);
        }
        cmbEspecialidad.setSelectedIndex(0);
        cmbMedico.removeAllItems();
        dateFecha.setDate(null);
        cmbHora.setSelectedIndex(0);
        cmbMotivo.setSelectedIndex(0);
    }
}
