package datos;

import modelo.Usuario;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaUsuarios {

    public static Usuario autenticar(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al autenticar usuario", ex);
        }
        return null;
    }

    public static ArrayList<Usuario> obtenerTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar usuarios", ex);
        }
        return lista;
    }

    private static Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("rol"),
            rs.getString("nombre_completo"),
            rs.getInt("entidad_id")
        );
    }
}
