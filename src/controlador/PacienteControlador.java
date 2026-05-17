package controlador;

import datos.ListaCitas;
import datos.ListaPacientes;
import modelo.Paciente;
import java.util.ArrayList;

public class PacienteControlador {

    public static String registrar(String nombre, String apellido,
                                   String dni, String telefono,
                                   String correo, String fechaNacimiento,
                                   String sexo, String tipoSeguro) {

        // Validar campos obligatorios
        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            return "ERROR: Nombre, apellido y DNI son obligatorios.";
        }

        if (dni.length() != 8) {
            return "ERROR: El DNI debe tener 8 dígitos.";
        }

        // Validar DNI duplicado
        if (ListaPacientes.existeDni(dni)) {
            return "ERROR: Ya existe un paciente registrado con el DNI " + dni + ".";
        }

        Paciente nuevo = new Paciente(0, nombre, apellido, dni,
                telefono, correo, fechaNacimiento, sexo, tipoSeguro);
        ListaPacientes.agregar(nuevo);

        // Polimorfismo: llamada a mostrarDatos() a través de referencia Persona
        nuevo.mostrarDatos();

        return "OK: Paciente " + nuevo.getNombreCompleto()
                + " registrado correctamente.";
    }

    public static String eliminar(int id) {
        Paciente p = ListaPacientes.buscarPorId(id);
        if (p == null) {
            return "ERROR: Paciente no encontrado.";
        }

        if (ListaCitas.tieneCitasPendientes(id)) {
            return "ERROR: El paciente tiene citas pendientes. "
                    + "Cancélelas antes de eliminar el registro.";
        }

        ListaPacientes.eliminar(id);
        return "OK: Paciente eliminado correctamente.";
    }

    public static String actualizar(int id, String nombre, String apellido,
                                    String dni, String telefono,
                                    String correo, String fechaNacimiento,
                                    String sexo, String tipoSeguro) {

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty()) {
            return "ERROR: Nombre, apellido y DNI son obligatorios.";
        }

        Paciente existente = ListaPacientes.buscarPorId(id);
        if (existente == null) {
            return "ERROR: Paciente no encontrado.";
        }

        // Verificar que el nuevo DNI no pertenezca a otro paciente
        Paciente conMismoDni = ListaPacientes.buscarPorDni(dni);
        if (conMismoDni != null && conMismoDni.getId() != id) {
            return "ERROR: El DNI " + dni
                    + " ya está registrado en otro paciente.";
        }

        existente.setNombre(nombre);
        existente.setApellido(apellido);
        existente.setDni(dni);
        existente.setTelefono(telefono);
        existente.setCorreo(correo);
        existente.setFechaNacimiento(fechaNacimiento);
        existente.setSexo(sexo);
        existente.setTipoSeguro(tipoSeguro);

        ListaPacientes.actualizar(existente);
        return "OK: Datos del paciente actualizados correctamente.";
    }

    public static ArrayList<Paciente> listar() {
        return ListaPacientes.obtenerTodos();
    }

    public static ArrayList<Paciente> buscarPorNombreODni(String texto) {
        ArrayList<Paciente> resultado = new ArrayList<>();
        for (Paciente p : ListaPacientes.obtenerTodos()) {
            if (p.getNombreCompleto().toLowerCase()
                    .contains(texto.toLowerCase())
                    || p.getDni().contains(texto)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public static Paciente buscarPorId(int id) {
        return ListaPacientes.buscarPorId(id);
    }

    public static int totalPacientes() {
        return ListaPacientes.totalRegistros();
    }
}