package modelo;

public class Urgencia {

    private int id;
    private String nombre;       // puede no estar registrado como Paciente
    private String dni;
    private String motivo;
    private String prioridad;    // Alta, Media, Baja
    private Medico medico;
    private String estado;       // En atención, Alta médica
    private String horaIngreso;  // formato: HH:mm

    public Urgencia() {
    }

    public Urgencia(int id, String nombre, String dni, String motivo,
                    String prioridad, Medico medico,
                    String estado, String horaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.motivo = motivo;
        this.prioridad = prioridad;
        this.medico = medico;
        this.estado = estado;
        this.horaIngreso = horaIngreso;
    }

    public void darAlta() {
        this.estado = "Alta médica";
    }

    public void asignarMedico(Medico medico) {
        this.medico = medico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    @Override
    public String toString() {
        return "Urgencia #" + id + " | " + nombre
               + " | Prioridad: " + prioridad
               + " | " + estado;
    }
}