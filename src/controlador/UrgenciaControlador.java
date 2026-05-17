package controlador;

import datos.ListaUrgencias;
import modelo.Medico;
import modelo.Urgencia;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UrgenciaControlador {

    public static String registrar(String nombre, String dni,
                                    String motivo, String prioridad,
                                    Medico medico) {

        if (nombre.isEmpty() || motivo.isEmpty()) {
            return "ERROR: Nombre y motivo son obligatorios.";
        }
        if (medico == null) {
            return "ERROR: Debe asignar un médico disponible.";
        }

        // Hora de ingreso automática
        String horaIngreso = LocalTime.now()
                .format(DateTimeFormatter.ofPattern("HH:mm"));

        Urgencia nueva = new Urgencia(0, nombre,
                (dni.isEmpty() ? "No registrado" : dni),
                motivo, prioridad, medico, "En atención", horaIngreso);

        ListaUrgencias.agregar(nueva);

        return "OK: Urgencia registrada.\n"
                + "Paciente         : " + nombre + "\n"
                + "DNI              : " + nueva.getDni() + "\n"
                + "Motivo           : " + motivo + "\n"
                + "Prioridad        : " + prioridad + "\n"
                + "Doctor asignado  : " + medico.getNombreCompleto() + "\n"
                + "Hora de ingreso  : " + horaIngreso;
    }

    public static String darAlta(int urgenciaId) {
        Urgencia u = ListaUrgencias.buscarPorId(urgenciaId);
        if (u == null) {
            return "ERROR: Urgencia no encontrada.";
        }
        if (u.getEstado().equals("Alta médica")) {
            return "ERROR: El paciente ya tiene alta médica registrada.";
        }
        u.darAlta();
        ListaUrgencias.actualizar(u);
        return "OK: Alta médica registrada para " + u.getNombre() + ".";
    }

    public static ArrayList<Urgencia> listarTodas() {
        return ListaUrgencias.obtenerTodas();
    }

    public static ArrayList<Urgencia> listarActivas() {
        return ListaUrgencias.obtenerActivas();
    }

    public static int totalActivas() {
        return ListaUrgencias.totalActivas();
    }
}