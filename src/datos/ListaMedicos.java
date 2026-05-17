package datos;

import modelo.Medico;
import modelo.Especialidad;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaMedicos {

    public static void agregar(Medico m) {
        String sql = "INSERT INTO medicos (nombre, apellido, dni, cmp, telefono, correo, turno, disponible, especialidad_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getApellido());
            ps.setString(3, m.getDni());
            ps.setString(4, m.getCmp());
            ps.setString(5, m.getTelefono());
            ps.setString(6, m.getCorreo());
            ps.setString(7, m.getTurno());
            ps.setBoolean(8, m.isDisponible());
            if (m.getEspecialidad() != null) {
                ps.setInt(9, m.getEspecialidad().getId());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar médico", ex);
        }
    }

    public static ArrayList<Medico> obtenerTodos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT m.*, e.id AS esp_id, e.nombre AS esp_nombre, e.descripcion AS esp_desc " +
                     "FROM medicos m LEFT JOIN especialidades e ON m.especialidad_id = e.id ORDER BY m.apellido, m.nombre";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearMedico(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar médicos", ex);
        }
        return lista;
    }

    public static Medico buscarPorId(int id) {
        String sql = "SELECT m.*, e.id AS esp_id, e.nombre AS esp_nombre, e.descripcion AS esp_desc " +
                     "FROM medicos m LEFT JOIN especialidades e ON m.especialidad_id = e.id WHERE m.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearMedico(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar médico por ID", ex);
        }
        return null;
    }

    public static Medico buscarPorCmp(String cmp) {
        String sql = "SELECT m.*, e.id AS esp_id, e.nombre AS esp_nombre, e.descripcion AS esp_desc " +
                     "FROM medicos m LEFT JOIN especialidades e ON m.especialidad_id = e.id WHERE m.cmp = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cmp);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearMedico(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar médico por CMP", ex);
        }
        return null;
    }

    public static boolean existeCmp(String cmp) {
        String sql = "SELECT COUNT(*) FROM medicos WHERE cmp = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cmp);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar CMP", ex);
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM medicos WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar médico", ex);
        }
    }

    public static void actualizar(Medico m) {
        String sql = "UPDATE medicos SET nombre=?, apellido=?, dni=?, cmp=?, telefono=?, correo=?, turno=?, disponible=?, especialidad_id=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getApellido());
            ps.setString(3, m.getDni());
            ps.setString(4, m.getCmp());
            ps.setString(5, m.getTelefono());
            ps.setString(6, m.getCorreo());
            ps.setString(7, m.getTurno());
            ps.setBoolean(8, m.isDisponible());
            if (m.getEspecialidad() != null) {
                ps.setInt(9, m.getEspecialidad().getId());
            } else {
                ps.setNull(9, Types.INTEGER);
            }
            ps.setInt(10, m.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar médico", ex);
        }
    }

    public static ArrayList<Medico> obtenerDisponibles() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "SELECT m.*, e.id AS esp_id, e.nombre AS esp_nombre, e.descripcion AS esp_desc " +
                     "FROM medicos m LEFT JOIN especialidades e ON m.especialidad_id = e.id WHERE m.disponible = TRUE ORDER BY m.apellido, m.nombre";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearMedico(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar médicos disponibles", ex);
        }
        return lista;
    }

    public static int totalRegistros() {
        String sql = "SELECT COUNT(*) FROM medicos";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar médicos", ex);
        }
    }

    private static Medico mapearMedico(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setApellido(rs.getString("apellido"));
        m.setDni(rs.getString("dni"));
        m.setCmp(rs.getString("cmp"));
        m.setTelefono(rs.getString("telefono"));
        m.setCorreo(rs.getString("correo"));
        m.setTurno(rs.getString("turno"));
        m.setDisponible(rs.getBoolean("disponible"));

        int espId = rs.getInt("esp_id");
        if (!rs.wasNull()) {
            Especialidad e = new Especialidad(espId, rs.getString("esp_nombre"), rs.getString("esp_desc"));
            m.setEspecialidad(e);
        }
        return m;
    }
}
