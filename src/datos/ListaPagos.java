package datos;

import modelo.Cita;
import modelo.Medico;
import modelo.Paciente;
import modelo.Pago;
import modelo.Especialidad;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;

public class ListaPagos {

    public static void agregar(Pago p) {
        String sql = "INSERT INTO pagos (cita_id, monto, metodo_pago, fecha) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getCita().getId());
            ps.setDouble(2, p.getMonto());
            ps.setString(3, p.getMetodoPago());
            ps.setString(4, p.getFecha());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar pago", ex);
        }
    }

    public static ArrayList<Pago> obtenerTodos() {
        ArrayList<Pago> lista = new ArrayList<>();
        String sql = "SELECT pg.*, " +
                     "c.id AS c_id, c.fecha AS c_fec, c.hora AS c_hor, c.estado AS c_est, c.motivo_consulta AS c_mot, c.observaciones AS c_obs, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM pagos pg " +
                     "JOIN citas c ON pg.cita_id = c.id " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "ORDER BY pg.fecha DESC";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearPago(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al listar pagos", ex);
        }
        return lista;
    }

    public static Pago buscarPorId(int id) {
        String sql = "SELECT pg.*, " +
                     "c.id AS c_id, c.fecha AS c_fec, c.hora AS c_hor, c.estado AS c_est, c.motivo_consulta AS c_mot, c.observaciones AS c_obs, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM pagos pg " +
                     "JOIN citas c ON pg.cita_id = c.id " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "WHERE pg.id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPago(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar pago por ID", ex);
        }
        return null;
    }

    public static boolean citaYaPagada(int citaId) {
        String sql = "SELECT COUNT(*) FROM pagos WHERE cita_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, citaId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al verificar pago", ex);
        }
    }

    public static ArrayList<Pago> buscarPorFecha(String fecha) {
        ArrayList<Pago> lista = new ArrayList<>();
        String sql = "SELECT pg.*, " +
                     "c.id AS c_id, c.fecha AS c_fec, c.hora AS c_hor, c.estado AS c_est, c.motivo_consulta AS c_mot, c.observaciones AS c_obs, " +
                     "p.id AS p_id, p.nombre AS p_nom, p.apellido AS p_ape, p.dni AS p_dni, p.telefono AS p_tel, p.correo AS p_cor, p.fecha_nacimiento AS p_fn, p.sexo AS p_sex, p.tipo_seguro AS p_seg, " +
                     "m.id AS m_id, m.nombre AS m_nom, m.apellido AS m_ape, m.dni AS m_dni, m.cmp AS m_cmp, m.telefono AS m_tel, m.correo AS m_cor, m.turno AS m_tur, m.disponible AS m_dis, " +
                     "e.id AS e_id, e.nombre AS e_nom, e.descripcion AS e_desc " +
                     "FROM pagos pg " +
                     "JOIN citas c ON pg.cita_id = c.id " +
                     "JOIN pacientes p ON c.paciente_id = p.id " +
                     "JOIN medicos m ON c.medico_id = m.id " +
                     "LEFT JOIN especialidades e ON m.especialidad_id = e.id " +
                     "WHERE pg.fecha = ? ORDER BY pg.fecha DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPago(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al buscar pagos por fecha", ex);
        }
        return lista;
    }

    public static double totalIngresos() {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos";
        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getDouble(1) : 0;
        } catch (SQLException ex) {
            throw new RuntimeException("Error al calcular ingresos", ex);
        }
    }

    public static double totalIngresosPorFecha(String fecha) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE fecha = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fecha);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al calcular ingresos por fecha", ex);
        }
    }

    private static Pago mapearPago(ResultSet rs) throws SQLException {
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
        c.setId(rs.getInt("c_id"));
        c.setPaciente(p);
        c.setMedico(m);
        c.setFecha(rs.getString("c_fec"));
        c.setHora(rs.getString("c_hor"));
        c.setEstado(rs.getString("c_est"));
        c.setMotivo(rs.getString("c_mot"));
        c.setObservaciones(rs.getString("c_obs"));

        Pago pg = new Pago();
        pg.setId(rs.getInt("id"));
        pg.setCita(c);
        pg.setMonto(rs.getDouble("monto"));
        pg.setMetodoPago(rs.getString("metodo_pago"));
        pg.setFecha(rs.getString("fecha"));
        return pg;
    }
}
