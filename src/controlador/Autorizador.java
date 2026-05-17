package controlador;

import modelo.Usuario;

public class Autorizador {

    public static final String ADMIN = "Administrador";
    public static final String RECEPCIONISTA = "Recepcionista";
    public static final String MEDICO = "Médico";
    public static final String CAJERO = "Cajero";
    public static final String PACIENTE = "Paciente";

    public static boolean puedeAcceder(String rol, String modulo) {
        if (rol == null || modulo == null) return false;

        switch (modulo) {
            case "dashboard":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA)
                    || rol.equals(MEDICO) || rol.equals(CAJERO)
                    || rol.equals(PACIENTE);

            case "pacientes":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA);

            case "medicos":
                return rol.equals(ADMIN);

            case "especialidades":
                return rol.equals(ADMIN);

            case "horarios":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA)
                    || rol.equals(MEDICO);

            case "agendar_cita":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA);

            case "gestion_citas":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA)
                    || rol.equals(MEDICO) || rol.equals(PACIENTE);

            case "urgencias":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA)
                    || rol.equals(MEDICO);

            case "historial":
                return rol.equals(ADMIN) || rol.equals(RECEPCIONISTA)
                    || rol.equals(MEDICO);

            case "pagos":
                return rol.equals(ADMIN) || rol.equals(CAJERO)
                    || rol.equals(RECEPCIONISTA);

            case "reportes":
                return rol.equals(ADMIN) || rol.equals(CAJERO);

            case "mis_citas":
                return rol.equals(PACIENTE);

            default:
                return false;
        }
    }

    public static boolean esAdmin(Usuario u) {
        return u != null && ADMIN.equals(u.getRol());
    }

    public static boolean esMedico(Usuario u) {
        return u != null && MEDICO.equals(u.getRol());
    }

    public static boolean esPaciente(Usuario u) {
        return u != null && PACIENTE.equals(u.getRol());
    }

    public static boolean esCajero(Usuario u) {
        return u != null && CAJERO.equals(u.getRol());
    }

    public static boolean esRecepcionista(Usuario u) {
        return u != null && RECEPCIONISTA.equals(u.getRol());
    }

    public static boolean puedeEditar(Usuario u, String modulo) {
        if (u == null) return false;
        if (esAdmin(u)) return true;
        if (esMedico(u)) {
            return "historial".equals(modulo) || "gestion_citas".equals(modulo);
        }
        if (esRecepcionista(u)) {
            return "pacientes".equals(modulo) || "horarios".equals(modulo)
                || "agendar_cita".equals(modulo) || "gestion_citas".equals(modulo)
                || "urgencias".equals(modulo);
        }
        if (esCajero(u)) {
            return "pagos".equals(modulo);
        }
        return false;
    }
}
