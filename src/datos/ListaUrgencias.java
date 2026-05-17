package datos;

import modelo.Urgencia;
import modelo.Medico;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaUrgencias {

    public static void agregar(Urgencia u) {
        String sql = "INSERT INTO urgencias (paciente_nombre, paciente_apellido, paciente_dni, paciente_telefono, medico_id, fecha, hora, motivo_urgencia, prioridad, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            String[] partes = u.getNombre().split(" ", 2);
            ps.setString(1, partes.length > 0 ? partes[0] : u.getNombre());
            ps.setString(2, partes.length > 1 ? partes[1] : "");
            ps.setString(3, u.getDni());
            ps.setString(4, "");
            if (u.getMedico() != null) {
                ps.setInt(5, u.getMedico().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            ps.setString(6, "");
            ps.setString(7, u.getHoraIngreso());
            ps.setString(8, u.getMotivo());
            ps.setString(9, u.getPrioridad());
            ps.setString(10, u.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) u.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar urgencia", ex);
        }
    }

    public static ArrayList<Urgencia> obtenerTodas() {
        ArrayList<Urgencia> lista = new ArrayList<>();
        String sql = "SELECT u.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp " +
                     "FROM urgencias u LEFT JOIN medicos m ON u.medico_id = m.id ORDER BY u.id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUrgencia(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar urgencias", ex);
        }
        return lista;
    }

    public static ArrayList<Urgencia> obtenerActivas() {
        ArrayList<Urgencia> lista = new ArrayList<>();
        String sql = "SELECT u.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp " +
                     "FROM urgencias u LEFT JOIN medicos m ON u.medico_id = m.id WHERE u.estado = 'En atención' ORDER BY u.id DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearUrgencia(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar urgencias activas", ex);
        }
        return lista;
    }

    public static Urgencia buscarPorId(int id) {
        String sql = "SELECT u.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp " +
                     "FROM urgencias u LEFT JOIN medicos m ON u.medico_id = m.id WHERE u.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUrgencia(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar urgencia por ID", ex);
        }
        return null;
    }

    public static void actualizar(Urgencia u) {
        String sql = "UPDATE urgencias SET medico_id=?, estado=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (u.getMedico() != null) {
                ps.setInt(1, u.getMedico().getId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, u.getEstado());
            ps.setInt(3, u.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar urgencia", ex);
        }
    }

    public static int totalActivas() {
        String sql = "SELECT COUNT(*) FROM urgencias WHERE estado = 'En atención'";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar urgencias activas", ex);
        }
    }

    private static Urgencia mapearUrgencia(ResultSet rs) throws SQLException {
        String nombreCompleto = rs.getString("paciente_nombre")
                + " " + rs.getString("paciente_apellido");

        Medico m = null;
        int medId = rs.getInt("m_id");
        if (!rs.wasNull()) {
            m = new Medico();
            m.setId(medId);
            m.setNombre(rs.getString("m_nom"));
            m.setApellido(rs.getString("m_ape"));
            m.setDni(rs.getString("m_dni"));
            m.setCmp(rs.getString("m_cmp"));
        }

        Urgencia u = new Urgencia();
        u.setId(rs.getInt("id"));
        u.setNombre(nombreCompleto);
        u.setDni(rs.getString("paciente_dni"));
        u.setMotivo(rs.getString("motivo_urgencia"));
        u.setPrioridad(rs.getString("prioridad"));
        u.setMedico(m);
        u.setEstado(rs.getString("estado"));
        u.setHoraIngreso(rs.getString("hora"));
        return u;
    }
}
