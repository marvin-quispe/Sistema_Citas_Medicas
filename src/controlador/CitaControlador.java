package controlador;

import datos.ListaCitas;
import modelo.Cita;
import modelo.Especialidad;
import modelo.Medico;
import modelo.Paciente;
import java.util.ArrayList;

public class CitaControlador {

    public static String agendar(Paciente paciente, Medico medico,
                                  Especialidad especialidad, String fecha,
                                  String hora, String motivo) {

        if (paciente == null || medico == null) {
            return "ERROR: Debe seleccionar un paciente y un médico.";
        }

        if (fecha.isEmpty() || hora.isEmpty() || motivo.isEmpty()) {
            return "ERROR: Fecha, hora y motivo son obligatorios.";
        }

        // Validar conflicto de horario (RF-20)
        if (ListaCitas.existeConflicto(medico.getId(), fecha, hora, -1)) {
            return "ERROR: El doctor " + medico.getNombreCompleto()
                    + " ya tiene una cita el " + fecha
                    + " a las " + hora
                    + ". Seleccione otra hora.";
        }

        Cita nueva = new Cita(0, paciente, medico, especialidad,
                fecha, hora, motivo, "Pendiente");
        ListaCitas.agregar(nueva);

        return "OK: Cita registrada correctamente.\n"
                + "Paciente  : " + paciente.getNombreCompleto() + "\n"
                + "Doctor    : " + medico.getNombreCompleto() + "\n"
                + "Fecha     : " + fecha + "\n"
                + "Hora      : " + hora + "\n"
                + "Motivo    : " + motivo;
    }

    public static String cancelar(int citaId) {
        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: Cita no encontrada.";
        }
        if (!c.getEstado().equals("Pendiente")) {
            return "ERROR: Solo se pueden cancelar citas en estado Pendiente.";
        }
        c.cancelar();
        ListaCitas.actualizar(c);
        return "OK: Cita cancelada. El horario queda disponible.";
    }

    public static String pagar(int citaId) {
        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: Cita no encontrada.";
        }
        if (!c.getEstado().equals("Pendiente")) {
            return "ERROR: Solo se pueden pagar citas en estado Pendiente.";
        }
        if (datos.ListaPagos.citaYaPagada(citaId)) {
            return "ERROR: Esta cita ya tiene un pago registrado.";
        }
        c.pagar();
        ListaCitas.actualizar(c);
        return "OK: Cita marcada como Pagada.\n"
                + "Paciente : " + c.getPaciente().getNombreCompleto() + "\n"
                + "Doctor   : " + c.getMedico().getNombreCompleto() + "\n"
                + "Pase al consultorio para ser atendido.";
    }

    public static String atender(int citaId) {
        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: Cita no encontrada.";
        }
        if (!c.getEstado().equals("Pagado")) {
            return "ERROR: No se puede atender. La cita no tiene pago registrado.\nEl paciente debe pasar primero por Caja.";
        }
        c.atender();
        ListaCitas.actualizar(c);
        return "OK: Cita marcada como Atendida.\n"
                + "Paciente : " + c.getPaciente().getNombreCompleto() + "\n"
                + "Doctor   : " + c.getMedico().getNombreCompleto();
    }

    public static String reprogramar(int citaId, String nuevaFecha,
                                      String nuevaHora) {
        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: Cita no encontrada.";
        }
        if (nuevaFecha.isEmpty() || nuevaHora.isEmpty()) {
            return "ERROR: Debe ingresar la nueva fecha y hora.";
        }

        // Validar conflicto excluyendo la cita actual
        if (ListaCitas.existeConflicto(c.getMedico().getId(),
                nuevaFecha, nuevaHora, citaId)) {
            return "ERROR: El doctor ya tiene cita el "
                    + nuevaFecha + " a las " + nuevaHora + ".";
        }

        c.reprogramar(nuevaFecha, nuevaHora);
        ListaCitas.actualizar(c);
        return "OK: Cita reprogramada para el " + nuevaFecha
                + " a las " + nuevaHora + ".";
    }

    public static String registrarObservaciones(int citaId,
                                                 String observaciones) {
        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: Cita no encontrada.";
        }
        if (observaciones.isEmpty()) {
            return "ERROR: Las observaciones no pueden estar vacías.";
        }
        c.setObservaciones(observaciones);
        ListaCitas.actualizar(c);
        return "OK: Observaciones registradas en el historial.";
    }

    public static ArrayList<Cita> listarTodas() {
        return ListaCitas.obtenerTodas();
    }

    public static ArrayList<Cita> listarPorEstado(String estado) {
        return ListaCitas.buscarPorEstado(estado);
    }

    public static ArrayList<Cita> listarPorPaciente(int pacienteId) {
        return ListaCitas.buscarPorPaciente(pacienteId);
    }

    public static ArrayList<Cita> listarPorMedico(int medicoId) {
        return ListaCitas.buscarPorMedico(medicoId);
    }

    public static ArrayList<Cita> listarPorFecha(String fecha) {
        return ListaCitas.buscarPorFecha(fecha);
    }

    public static Cita buscarPorId(int id) {
        return ListaCitas.buscarPorId(id);
    }

    public static int totalCitas() {
        return ListaCitas.totalRegistros();
    }

    public static int totalPorEstado(String estado) {
        return ListaCitas.totalPorEstado(estado);
    }
}