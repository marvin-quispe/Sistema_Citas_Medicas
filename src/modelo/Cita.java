package modelo;

public class Cita {

    private int id;
    private Paciente paciente;
    private Medico medico;
    private Especialidad especialidad;
    private String fecha;       // formato: yyyy-MM-dd
    private String hora;        // formato: HH:mm
    private String motivo;
    private String estado;      // Pendiente, Pagado, Atendida, Cancelada, Reprogramada
    private String observaciones;

    public Cita() {
    }

    public Cita(int id, Paciente paciente, Medico medico,
                Especialidad especialidad, String fecha, String hora,
                String motivo, String estado) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = estado;
        this.observaciones = "";
    }

    public void cancelar() {
        this.estado = "Cancelada";
    }

    public void pagar() {
        this.estado = "Pagado";
    }

    public void atender() {
        this.estado = "Atendida";
    }

    public void reprogramar(String nuevaFecha, String nuevaHora) {
        this.fecha = nuevaFecha;
        this.hora = nuevaHora;
        this.estado = "Reprogramada";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Cita #" + id + " | "
               + (paciente != null ? paciente.getNombreCompleto() : "—")
               + " | " + fecha + " " + hora
               + " | " + estado;
    }
}