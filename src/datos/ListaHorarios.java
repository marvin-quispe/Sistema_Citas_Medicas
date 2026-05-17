package datos;

import modelo.Horario;
import modelo.Medico;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaHorarios {

    public static void agregar(Horario h) {
        String sql = "INSERT INTO horarios (medico_id, dia_semana, turno) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getMedico().getId());
            ps.setString(2, h.getDiaSemana());
            ps.setString(3, h.getTurno());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) h.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar horario", ex);
        }
    }

    public static ArrayList<Horario> obtenerTodos() {
        ArrayList<Horario> lista = new ArrayList<>();
        String sql = "SELECT h.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis " +
                     "FROM horarios h JOIN medicos m ON h.medico_id = m.id ORDER BY m.apellido, h.dia_semana, h.turno";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearHorario(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar horarios", ex);
        }
        return lista;
    }

    public static Horario buscarPorId(int id) {
        String sql = "SELECT h.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis " +
                     "FROM horarios h JOIN medicos m ON h.medico_id = m.id WHERE h.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearHorario(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar horario por ID", ex);
        }
        return null;
    }

    public static ArrayList<Horario> buscarPorMedico(int medicoId) {
        ArrayList<Horario> lista = new ArrayList<>();
        String sql = "SELECT h.*, m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis " +
                     "FROM horarios h JOIN medicos m ON h.medico_id = m.id WHERE h.medico_id = ? ORDER BY h.dia_semana, h.turno";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearHorario(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar horarios por médico", ex);
        }
        return lista;
    }

    public static boolean existeHorario(int medicoId, String dia, String turno) {
        String sql = "SELECT COUNT(*) FROM horarios WHERE medico_id = ? AND dia_semana = ? AND turno = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setString(2, dia);
            ps.setString(3, turno);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar horario", ex);
        }
    }

    public static boolean tieneCitasAsignadas(int horarioId) {
        Horario h = buscarPorId(horarioId);
        if (h == null) return false;
        String sql = "SELECT COUNT(*) FROM citas WHERE medico_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.getMedico().getId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar citas asignadas", ex);
        }
    }

    public static boolean eliminar(int id) {
        String sql = "DELETE FROM horarios WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al eliminar horario", ex);
        }
    }

    public static void actualizar(Horario h) {
        String sql = "UPDATE horarios SET medico_id=?, dia_semana=?, turno=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, h.getMedico().getId());
            ps.setString(2, h.getDiaSemana());
            ps.setString(3, h.getTurno());
            ps.setInt(4, h.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar horario", ex);
        }
    }

    private static Horario mapearHorario(ResultSet rs) throws SQLException {
        Medico m = new Medico();
        m.setId(rs.getInt("m_id"));
        m.setNombre(rs.getString("m_nom"));
        m.setApellido(rs.getString("m_ape"));
        m.setDni(rs.getString("m_dni"));
        m.setCmp(rs.getString("m_cmp"));
        m.setTelefono(rs.getString("m_tel"));
        m.setCorreo(rs.getString("m_cor"));
        m.setTurno(rs.getString("m_tur"));
        m.setDisponible(rs.getBoolean("m_dis"));

        Horario h = new Horario();
        h.setId(rs.getInt("id"));
        h.setMedico(m);
        h.setDiaSemana(rs.getString("dia_semana"));
        h.setTurno(rs.getString("turno"));
        h.setDisponible(true);
        return h;
    }
}
