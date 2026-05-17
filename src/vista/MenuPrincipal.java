package vista;

import controlador.Autorizador;
import modelo.Usuario;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.JOptionPane;



public class MenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;	
	private JPanel contentPane;
    private JPanel panelContenido;
    private Usuario usuarioSesion;
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Usuario prueba = new Usuario(1, "admin", "1234", "Administrador", "Marvin Admin");
				MenuPrincipal frame = new MenuPrincipal(prueba);
				frame.setVisible(true);
			}
		});
	}

	public MenuPrincipal(Usuario sesion) {
		this.usuarioSesion = sesion;	

		setTitle("Clínica Grupo_04 — Sistema de Citas Médicas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 680);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panelLateral = new JPanel();
		panelLateral.setBackground(new Color(0, 128, 192));
		panelLateral.setBounds(10, 11, 184, 599);
		contentPane.add(panelLateral);
		panelLateral.setLayout(null);
		
		JLabel lblNombreUser = new JLabel(sesion.getNombre());
		lblNombreUser.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNombreUser.setForeground(new Color(255, 255, 255));
		lblNombreUser.setBounds(10, 31, 160, 14);
		panelLateral.add(lblNombreUser);
		
		JLabel lblBienvenido = new JLabel("Bienvenido");
		lblBienvenido.setForeground(new Color(255, 255, 255));
		lblBienvenido.setBounds(32, 11, 67, 15);
		lblBienvenido.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelLateral.add(lblBienvenido);
		
		JLabel lblRolUser = new JLabel("Rol: " + sesion.getRol());
		lblRolUser.setForeground(new Color(255, 255, 255));
		lblRolUser.setBounds(10, 42, 160, 14);
		panelLateral.add(lblRolUser);
		
		// ── PERMISOS POR ROL ─────────────────────────────────────────
		boolean showDashboard = Autorizador.puedeAcceder(sesion.getRol(), "dashboard");
		boolean showPacientes = Autorizador.puedeAcceder(sesion.getRol(), "pacientes");
		boolean showMedicos = Autorizador.puedeAcceder(sesion.getRol(), "medicos");
		boolean showEspecialidades = Autorizador.puedeAcceder(sesion.getRol(), "especialidades");
		boolean showHorarios = Autorizador.puedeAcceder(sesion.getRol(), "horarios");
		boolean showAgendarCita = Autorizador.puedeAcceder(sesion.getRol(), "agendar_cita");
		boolean showPagos = Autorizador.puedeAcceder(sesion.getRol(), "pagos");
		boolean showGestionCitas = Autorizador.puedeAcceder(sesion.getRol(), "gestion_citas");
		boolean showUrgencias = Autorizador.puedeAcceder(sesion.getRol(), "urgencias");
		boolean showHistorial = Autorizador.puedeAcceder(sesion.getRol(), "historial");
		boolean showReportes = Autorizador.puedeAcceder(sesion.getRol(), "reportes");
		
		// ── GRUPO 1: REGISTROS ───────────────────────────────────────
		int y = 67;
		if (showPacientes || showMedicos || showEspecialidades || showHorarios) {
			JLabel lblGrupo1 = new JLabel("REGISTROS");
			lblGrupo1.setFont(new Font("Tahoma", Font.BOLD, 9));
			lblGrupo1.setForeground(new Color(200, 220, 240));
			lblGrupo1.setBounds(10, y, 67, 14);
			panelLateral.add(lblGrupo1);
			y += 17;
			
			JSeparator sep1 = new JSeparator();
			sep1.setBounds(0, y - 6, 184, 1);
			panelLateral.add(sep1);
		}
		
		if (showPacientes) {
			JButton btnPacientes = new JButton("Pacientes");
			btnPacientes.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnPacientes.setBackground(new Color(0, 128, 192));
			btnPacientes.setForeground(new Color(255, 255, 255));
			btnPacientes.setBorderPainted(false);
			btnPacientes.setFocusPainted(false);
			btnPacientes.setOpaque(true);
			btnPacientes.setBounds(0, y, 184, 31);
			btnPacientes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new PacientesPanel());
				}
			});
			panelLateral.add(btnPacientes);
			y += 31;
		}
		
		if (showMedicos) {
			JButton btnMedicos = new JButton("Médicos");
			btnMedicos.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnMedicos.setBackground(new Color(0, 128, 192));
			btnMedicos.setForeground(new Color(255, 255, 255));
			btnMedicos.setBorderPainted(false);
			btnMedicos.setFocusPainted(false);
			btnMedicos.setOpaque(true);
			btnMedicos.setBounds(0, y, 184, 31);
			btnMedicos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new MedicosPanel());
				}
			});		
			panelLateral.add(btnMedicos);
			y += 31;
		}
		
		if (showEspecialidades) {
			JButton btnEspecialid = new JButton("Especialidades");
			btnEspecialid.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnEspecialid.setBackground(new Color(0, 128, 192));
			btnEspecialid.setForeground(new Color(255, 255, 255));
			btnEspecialid.setBorderPainted(false);
			btnEspecialid.setFocusPainted(false);
			btnEspecialid.setOpaque(true);
			btnEspecialid.setBounds(0, y, 184, 31);
			btnEspecialid.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new EspecialidadesPanel());
				}
			});
			panelLateral.add(btnEspecialid);
			y += 31;
		}
		
		if (showHorarios) {
			JButton btnHorarios = new JButton("Horarios");
			btnHorarios.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnHorarios.setBackground(new Color(0, 128, 192));
			btnHorarios.setForeground(new Color(255, 255, 255));
			btnHorarios.setBorderPainted(false);
			btnHorarios.setFocusPainted(false);
			btnHorarios.setOpaque(true);
			btnHorarios.setBounds(0, y, 184, 31);
			btnHorarios.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new HorariosPanel(usuarioSesion));
				}
			});
			panelLateral.add(btnHorarios);
			y += 31;
		}
		
		// ── GRUPO 2: ATENCIÓN ────────────────────────────────────────
		y += 10;
		if (showAgendarCita || showPagos || showGestionCitas || showUrgencias) {
			JLabel lblGrupo2 = new JLabel("ATENCION");
			lblGrupo2.setFont(new Font("Tahoma", Font.BOLD, 9));
			lblGrupo2.setForeground(new Color(220, 220, 240));
			lblGrupo2.setBounds(10, y, 67, 14);
			panelLateral.add(lblGrupo2);
			y += 17;
		}
		
		if (showAgendarCita) {
			JButton btnCitas = new JButton("Agendar Cita");
			btnCitas.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnCitas.setBackground(new Color(0, 128, 192));
			btnCitas.setForeground(new Color(255, 255, 255));
			btnCitas.setBorderPainted(false);
			btnCitas.setFocusPainted(false);
			btnCitas.setOpaque(true);
			btnCitas.setBounds(0, y, 184, 31);
			btnCitas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new AgendarCitaPanel());
				}
			});
			panelLateral.add(btnCitas);
			y += 31;
		}
		
		if (showPagos) {
			JButton btnPagos = new JButton("Caja — Pagos");
			btnPagos.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnPagos.setBackground(new Color(0, 128, 192));
			btnPagos.setForeground(new Color(255, 255, 255));
			btnPagos.setBorderPainted(false);
			btnPagos.setFocusPainted(false);
			btnPagos.setOpaque(true);
			btnPagos.setBounds(0, y, 184, 31);
			btnPagos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new PagosPanel());
				}
			});
			panelLateral.add(btnPagos);
			y += 31;
		}
		
		if (showGestionCitas) {
			JButton btnGestion = new JButton("Consultorio");
			btnGestion.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnGestion.setBackground(new Color(0, 128, 192));
			btnGestion.setForeground(new Color(255, 255, 255));
			btnGestion.setBorderPainted(false);
			btnGestion.setFocusPainted(false);
			btnGestion.setOpaque(true);
			btnGestion.setBounds(0, y, 184, 31);
			btnGestion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (Autorizador.esMedico(usuarioSesion)) {
						cargarPanel(new PanelDoctorGenerico(usuarioSesion));
					} else {
						cargarPanel(new GestionCitasPanel(usuarioSesion));
					}
				}
			});
			panelLateral.add(btnGestion);
			y += 31;
		}
		
		if (showUrgencias) {
			JButton btnUrgencias = new JButton("Urgencias");
			btnUrgencias.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnUrgencias.setBackground(new Color(0, 128, 192));
			btnUrgencias.setForeground(new Color(255, 255, 255));
			btnUrgencias.setBorderPainted(false);
			btnUrgencias.setFocusPainted(false);
			btnUrgencias.setOpaque(true);
			btnUrgencias.setBounds(0, y, 184, 31);
			btnUrgencias.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new UrgenciasPanel());
				}
			});
			panelLateral.add(btnUrgencias);
			y += 31;
		}
		
		// ── GRUPO 3: CLÍNICA ─────────────────────────────────────────
		y += 10;
		if (showHistorial || showReportes) {
			JLabel lblGrupo3 = new JLabel("CLINICA");
			lblGrupo3.setFont(new Font("Tahoma", Font.BOLD, 9));
			lblGrupo3.setForeground(new Color(200, 220, 240));
			lblGrupo3.setBounds(10, y, 46, 14);
			panelLateral.add(lblGrupo3);
			y += 17;
		}
		
		if (showHistorial) {
			JButton btnHistorial = new JButton("Historial");
			btnHistorial.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnHistorial.setBackground(new Color(0, 128, 192));
			btnHistorial.setForeground(new Color(255, 255, 255));
			btnHistorial.setBorderPainted(false);
			btnHistorial.setFocusPainted(false);
			btnHistorial.setOpaque(true);
			btnHistorial.setBounds(0, y, 184, 31);
			btnHistorial.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new HistorialPanel(usuarioSesion));
				}
			});
			panelLateral.add(btnHistorial);
			y += 31;
		}
		
		if (showReportes) {
			JButton btnReportes = new JButton("Reportes");
			btnReportes.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnReportes.setBackground(new Color(0, 128, 192));
			btnReportes.setForeground(new Color(255, 255, 255));
			btnReportes.setBorderPainted(false);
			btnReportes.setFocusPainted(false);
			btnReportes.setOpaque(true);
			btnReportes.setBounds(0, y, 184, 31);
			btnReportes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new ReportesPanel());
				}
			});
			panelLateral.add(btnReportes);
		}
		
		// ── DASHBOARD (siempre al final antes de cerrar) ─────────────
		if (showDashboard) {
			JLabel lblGrupoInicio = new JLabel("INICIO");
			lblGrupoInicio.setFont(new Font("Tahoma", Font.BOLD, 9));
			lblGrupoInicio.setForeground(new Color(255, 255, 255));
			lblGrupoInicio.setBounds(10, 520, 46, 14);
			panelLateral.add(lblGrupoInicio);
			
			JButton btnDashboard = new JButton("Dashboard");
			btnDashboard.setFont(new Font("Tahoma", Font.PLAIN, 11));
			btnDashboard.setBackground(new Color(0, 128, 192));
			btnDashboard.setForeground(new Color(255, 255, 255));
			btnDashboard.setBorderPainted(false);
			btnDashboard.setFocusPainted(false);
			btnDashboard.setOpaque(true);
			btnDashboard.setBounds(0, 536, 184, 31);
			btnDashboard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cargarPanel(new DashboardPanel(usuarioSesion));
				}
			});	
			panelLateral.add(btnDashboard);
		}
		
		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.setFont(new Font("Tahoma", Font.BOLD, 9));
	    btnCerrarSesion.setBackground(new Color(0, 100, 160));
	    btnCerrarSesion.setForeground(new Color(255, 255, 255));
	    btnCerrarSesion.setBorderPainted(false);
	    btnCerrarSesion.setFocusPainted(false);
	    btnCerrarSesion.setOpaque(true);
	    btnCerrarSesion.setBounds(32, 560, 120, 23);
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "¿Desea cerrar sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Login login = new Login();
                    login.setVisible(true);
                    dispose();
                }
			}
		});
		panelLateral.add(btnCerrarSesion);
		
		panelContenido = new JPanel();
        panelContenido.setLayout(new java.awt.BorderLayout());
        panelContenido.setBackground(new Color(240, 244, 248));
        panelContenido.setBounds(204, 11, 720, 630);
		contentPane.add(panelContenido);
		
        // Cargar Dashboard al iniciar
        if (showDashboard) {
            DashboardPanel dashboardPanel = new DashboardPanel(usuarioSesion);
            cargarPanel(dashboardPanel);
            dashboardPanel.setLayout(null);
        }
    }
	
   	public void cargarPanel(JPanel panel) {
   		panelContenido.removeAll();
   		panelContenido.add(panel, java.awt.BorderLayout.CENTER);
   		panelContenido.revalidate();
   		panelContenido.repaint();
	}
}
