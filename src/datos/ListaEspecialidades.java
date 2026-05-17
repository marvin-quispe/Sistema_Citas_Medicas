package datos;

import modelo.Especialidad;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaEspecialidades {

    public static void agregar(Especialidad e) {
        String sql = "INSERT INTO especialidades (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) e.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar especialidad", ex);
        }
    }

    public static ArrayList<Especialidad> obtenerTodas() {
        ArrayList<Especialidad> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM especialidades ORDER BY nombre";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Especialidad e = new Especialidad(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar especialidades", ex);
        }
        return lista;
    }

    public static boolean existe(String nombre) {
        String sql = "SELECT COUNT(*) FROM especialidades WHERE nombre = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar especialidad", ex);
        }
    }
}
