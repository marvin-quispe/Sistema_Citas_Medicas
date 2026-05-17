package controlador;

import datos.ListaMedicos;
import modelo.Especialidad;
import modelo.Medico;
import java.util.ArrayList;

public class MedicoControlador {

    public static String registrar(String nombre, String apellido,
                                   String dni, String cmp,
                                   String telefono, String correo,
                                   String turno, Especialidad especialidad) {

        if (nombre.isEmpty() || apellido.isEmpty()
                || dni.isEmpty() || cmp.isEmpty()) {
            return "ERROR: Nombre, apellido, DNI y CMP son obligatorios.";
        }

        if (ListaMedicos.existeCmp(cmp)) {
            return "ERROR: Ya existe un médico registrado con el CMP " + cmp + ".";
        }

        Medico nuevo = new Medico(0, nombre, apellido, dni,
                cmp, telefono, correo, turno, true, especialidad);
        ListaMedicos.agregar(nuevo);

        // Polimorfismo: llamada a mostrarDatos() a través de referencia Persona
        nuevo.mostrarDatos();

        return "OK: Doctor " + nuevo.getNombreCompleto()
                + " registrado correctamente.";
    }

    public static String eliminar(int id) {
        Medico m = ListaMedicos.buscarPorId(id);
        if (m == null) {
            return "ERROR: Médico no encontrado.";
        }

        // Cuando conectes JDBC aquí verificarás citas activas del médico
        ListaMedicos.eliminar(id);
        return "OK: Médico eliminado correctamente.";
    }

    public static String actualizar(int id, String nombre, String apellido,
                                    String dni, String cmp,
                                    String telefono, String correo,
                                    String turno, Especialidad especialidad) {

        if (nombre.isEmpty() || apellido.isEmpty()
                || dni.isEmpty() || cmp.isEmpty()) {
            return "ERROR: Nombre, apellido, DNI y CMP son obligatorios.";
        }

        Medico existente = ListaMedicos.buscarPorId(id);
        if (existente == null) {
            return "ERROR: Médico no encontrado.";
        }

        Medico conMismoCmp = ListaMedicos.buscarPorCmp(cmp);
        if (conMismoCmp != null && conMismoCmp.getId() != id) {
            return "ERROR: El CMP " + cmp
                    + " ya está registrado en otro médico.";
        }

        existente.setNombre(nombre);
        existente.setApellido(apellido);
        existente.setDni(dni);
        existente.setCmp(cmp);
        existente.setTelefono(telefono);
        existente.setCorreo(correo);
        existente.setTurno(turno);
        existente.setEspecialidad(especialidad);

        ListaMedicos.actualizar(existente);
        return "OK: Datos del médico actualizados correctamente.";
    }

    public static ArrayList<Medico> listar() {
        return ListaMedicos.obtenerTodos();
    }

    public static ArrayList<Medico> listarDisponibles() {
        return ListaMedicos.obtenerDisponibles();
    }

    public static Medico buscarPorId(int id) {
        return ListaMedicos.buscarPorId(id);
    }

    public static int totalMedicos() {
        return ListaMedicos.totalRegistros();
    }
}