package datos;

import modelo.Paciente;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaPacientes {

    public static void agregar(Paciente p) {
        String sql = "INSERT INTO pacientes (nombre, apellido, dni, telefono, correo, fecha_nacimiento, sexo, tipo_seguro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getFechaNacimiento());
            ps.setString(7, p.getSexo());
            ps.setString(8, p.getTipoSeguro());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar paciente", ex);
        }
    }

    public static ArrayList<Paciente> obtenerTodos() {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY apellido, nombre";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPaciente(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar pacientes", ex);
        }
        return lista;
    }

    public static Paciente buscarPorDni(String dni) {
        String sql = "SELECT * FROM pacientes WHERE dni = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPaciente(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar paciente por DNI", ex);
        }
        return null;
    }

    public static Paciente buscarPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPaciente(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar paciente por ID", ex);
        }
        return null;
    }

    public static boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM pacientes WHERE dni = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar DNI", ex);
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM pacientes WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar paciente", ex);
        }
    }

    public static void actualizar(Paciente p) {
        String sql = "UPDATE pacientes SET nombre=?, apellido=?, dni=?, telefono=?, correo=?, fecha_nacimiento=?, sexo=?, tipo_seguro=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApellido());
            ps.setString(3, p.getDni());
            ps.setString(4, p.getTelefono());
            ps.setString(5, p.getCorreo());
            ps.setString(6, p.getFechaNacimiento());
            ps.setString(7, p.getSexo());
            ps.setString(8, p.getTipoSeguro());
            ps.setInt(9, p.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar paciente", ex);
        }
    }

    public static int totalRegistros() {
        String sql = "SELECT COUNT(*) FROM pacientes";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar pacientes", ex);
        }
    }

    private static Paciente mapearPaciente(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setDni(rs.getString("dni"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setFechaNacimiento(rs.getString("fecha_nacimiento"));
        p.setSexo(rs.getString("sexo"));
        p.setTipoSeguro(rs.getString("tipo_seguro"));
        return p;
    }
}
