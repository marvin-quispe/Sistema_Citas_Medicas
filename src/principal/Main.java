package principal;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JOptionPane;
import util.DatabaseConnection;
import vista.Login;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verificar conexión a MySQL antes de iniciar
        try {
            DatabaseConnection.getConnection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al conectar con MySQL.\n\n" +
                "1. Asegúrate de que MySQL esté ejecutándose.\n" +
                "2. Ejecuta el script sql/schema.sql en MySQL Workbench.\n" +
                "3. Verifica usuario/password en DatabaseConnection.java.\n\n" +
                "Detalle: " + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}