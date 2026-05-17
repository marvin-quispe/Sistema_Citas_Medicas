package datos;

import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import modelo.Especialidad;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaCitas {

    public static void agregar(Cita c) {
        String sql = "INSERT INTO citas (paciente_id, medico_id, fecha, hora, estado, motivo_consulta, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, c.getPaciente().getId());
            ps.setInt(2, c.getMedico().getId());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getEstado());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar cita", ex);
        }
    }

    public static ArrayList<Cita> obtenerTodas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "ORDER BY c.fecha DESC, c.hora DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearCita(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar citas", ex);
        }
        return lista;
    }

    public static Cita buscarPorId(int id) {
        String sql = "SELECT c.*, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "WHERE c.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCita(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar cita por ID", ex);
        }
        return null;
    }

    public static ArrayList<Cita> buscarPorPaciente(int pacienteId) {
        return buscarConFiltro("c.paciente_id = ?", pacienteId, null, null, null);
    }

    public static ArrayList<Cita> buscarPorMedico(int medicoId) {
        return buscarConFiltro("c.medico_id = ?", null, medicoId, null, null);
    }

    public static ArrayList<Cita> buscarPorFecha(String fecha) {
        return buscarConFiltro("c.fecha = ?", null, null, fecha, null);
    }

    public static ArrayList<Cita> buscarPorEstado(String estado) {
        return buscarConFiltro("c.estado = ?", null, null, null, estado);
    }

    public static boolean existeConflicto(int medicoId, String fecha, String hora, int citaIdExcluir) {
        String sql = "SELECT COUNT(*) FROM citas WHERE medico_id = ? AND fecha = ? AND hora = ? AND estado != 'Cancelada' AND id != ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            ps.setInt(4, citaIdExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar conflicto de horario", ex);
        }
    }

    public static boolean tieneCitasPendientes(int pacienteId) {
        String sql = "SELECT COUNT(*) FROM citas WHERE paciente_id = ? AND estado = 'Pendiente'";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar citas pendientes", ex);
        }
    }

    public static void actualizar(Cita c) {
        String sql = "UPDATE citas SET paciente_id=?, medico_id=?, fecha=?, hora=?, estado=?, motivo_consulta=?, observaciones=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getPaciente().getId());
            ps.setInt(2, c.getMedico().getId());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getEstado());
            ps.setString(6, c.getMotivo());
            ps.setString(7, c.getObservaciones());
            ps.setInt(8, c.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al actualizar cita", ex);
        }
    }

    public static int totalRegistros() {
        String sql = "SELECT COUNT(*) FROM citas";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar citas", ex);
        }
    }

    public static int totalPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM citas WHERE estado = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al contar citas por estado", ex);
        }
    }

    private static ArrayList<Cita> buscarConFiltro(String where, Integer pacienteId, Integer medicoId, String fecha, String estado) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.*, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM citas c " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "WHERE " + where + " ORDER BY c.fecha DESC, c.hora DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (pacienteId != null) ps.setInt(1, pacienteId);
            else if (medicoId != null) ps.setInt(1, medicoId);
            else if (fecha != null) ps.setString(1, fecha);
            else if (estado != null) ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearCita(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar citas", ex);
        }
        return lista;
    }

    private static Cita mapearCita(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("p_id"));
        p.setNombre(rs.getString("p_nom"));
        p.setApellido(rs.getString("p_ape"));
        p.setDni(rs.getString("p_dni"));
        p.setTelefono(rs.getString("p_tel"));
        p.setCorreo(rs.getString("p_cor"));
        p.setFechaNacimiento(rs.getString("p_fn"));
        p.setSexo(rs.getString("p_sex"));
        p.setTipoSeguro(rs.getString("p_seg"));

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

        int eId = rs.getInt("e_id");
        if (!rs.wasNull()) {
            Especialidad e = new Especialidad(eId, rs.getString("e_nom"), rs.getString("e_desc"));
            m.setEspecialidad(e);
        }

        Cita c = new Cita();
        c.setId(rs.getInt("id"));
        c.setPaciente(p);
        c.setMedico(m);
        c.setFecha(rs.getString("fecha"));
        c.setHora(rs.getString("hora"));
        c.setEstado(rs.getString("estado"));
        c.setMotivo(rs.getString("motivo_consulta"));
        c.setObservaciones(rs.getString("observaciones"));
        return c;
    }
}
