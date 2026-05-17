package controlador;

import datos.ListaCitas;
import datos.ListaPagos;
import modelo.Cita;
import modelo.Pago;
import java.util.ArrayList;

public class PagoControlador {

    public static String registrar(int citaId, double monto,
                                     String metodoPago, String fecha) {

        if (monto <= 0) {
            return "ERROR: El monto debe ser mayor a cero.";
        }
        if (metodoPago.isEmpty() || fecha.isEmpty()) {
            return "ERROR: Método de pago y fecha son obligatorios.";
        }

        Cita c = ListaCitas.buscarPorId(citaId);
        if (c == null) {
            return "ERROR: La cita seleccionada no existe.";
        }
        if (!c.getEstado().equals("Pendiente")) {
            return "ERROR: Solo se pueden registrar pagos de citas Pendientes.";
        }
        if (ListaPagos.citaYaPagada(citaId)) {
            return "ERROR: Esta cita ya tiene un pago registrado.";
        }

        // Registrar el pago y cambiar el estado de la cita a Pagado
        Pago nuevo = new Pago(0, c, monto, metodoPago, fecha);
        ListaPagos.agregar(nuevo);

        // Cambiar estado de la cita a Pagado (el paciente puede ir al consultorio)
        c.setEstado("Pagado");
        ListaCitas.actualizar(c);

        return "OK: Pago registrado correctamente.\n"
                + "Paciente    : " + c.getPaciente().getNombreCompleto() + "\n"
                + "Doctor      : " + c.getMedico().getNombreCompleto() + "\n"
                + "Monto       : S/ " + String.format("%.2f", monto) + "\n"
                + "Método      : " + metodoPago + "\n"
                + "Fecha       : " + fecha + "\n"
                + "Estado      : Pagado — Puede pasar al consultorio.";
    }

    public static ArrayList<Pago> listarTodos() {
        return ListaPagos.obtenerTodos();
    }

    public static ArrayList<Pago> listarPorFecha(String fecha) {
        return ListaPagos.buscarPorFecha(fecha);
    }

    public static double totalIngresos() {
        return ListaPagos.totalIngresos();
    }

    public static double totalIngresosPorFecha(String fecha) {
        return ListaPagos.totalIngresosPorFecha(fecha);
    }

    public static boolean citaYaPagada(int citaId) {
        return ListaPagos.citaYaPagada(citaId);
    }
}