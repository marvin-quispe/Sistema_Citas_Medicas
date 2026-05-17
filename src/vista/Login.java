package vista;

import controlador.UsuarioControlador;
import modelo.Usuario;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextPane;




public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 380, 420);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Clínica Grupo_04");
		lblNewLabel.setBounds(125, 55, 85, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblTitulo = new JLabel("SISTEMA DE CITAS MÉDICAS");
		lblTitulo.setBounds(104, 30, 140, 14);
		contentPane.add(lblTitulo);
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(65, 105, 46, 14);
		contentPane.add(lblUsuario);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(135, 102, 86, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblPassword = new JLabel("Contraseña :");
		lblPassword.setBounds(65, 152, 63, 14);
		contentPane.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(135, 149, 86, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblError = new JLabel("");
		lblError.setForeground(new Color(255, 0, 0));
		lblError.setBounds(65, 190, 216, 14);
		contentPane.add(lblError);
		
		JButton btnIngresar = new JButton("Ingresar");
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                String user = txtUsuario.getText().trim();
                String pass = new String(
                        txtPassword.getPassword()).trim();

                if (user.isEmpty() || pass.isEmpty()) {
                    lblError.setText(
                            "Ingrese usuario y contraseña.");
                    return;
                }

                Usuario sesion = UsuarioControlador
                        .autenticar(user, pass);

                if (sesion == null) {
                    lblError.setText(
                            "Usuario o contraseña incorrectos.");
                    txtPassword.setText("");
                    return;
                }

                // Login exitoso — abre el menú principal
                lblError.setText("");
                MenuPrincipal menu = new MenuPrincipal(sesion);
                menu.setVisible(true);
                dispose(); // cierra el login
                // ─────────────────────────────────────────────

            }
		});
		btnIngresar.setBounds(132, 215, 89, 23);
		contentPane.add(btnIngresar);
		
		JLabel lblNewLabel_1 = new JLabel("1. Usuario: admin            / contraseña:1234");
		lblNewLabel_1.setBounds(21, 271, 260, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("2. Usuario: recepcionista/ contraseña:1234");
		lblNewLabel_1_1.setBounds(21, 285, 260, 14);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("3. Usuario: medico          / contraseña:1234");
		lblNewLabel_1_1_1.setBounds(21, 299, 260, 14);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("4. Usuario: cajero          / contraseña:1234");
		lblNewLabel_1_1_2.setBounds(21, 315, 260, 14);
		contentPane.add(lblNewLabel_1_1_2);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("5. Usuario: paciente      / contraseña:1234");
		lblNewLabel_1_1_2_1.setBounds(21, 331, 260, 14);
		contentPane.add(lblNewLabel_1_1_2_1);

	}
}
