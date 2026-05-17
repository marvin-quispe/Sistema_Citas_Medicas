package modelo;

public class HistorialClinico {

    private int id;
    private Cita cita;
    private String observaciones;
    private String fecha;   // fecha del registro

    public HistorialClinico() {
    }

    public HistorialClinico(int id, Cita cita, String observaciones, String fecha) {
        this.id = id;
        this.cita = cita;
        this.observaciones = observaciones;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cita getCita() {
        return cita;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Historial #" + id + " | Cita: "
               + (cita != null ? cita.getId() : "—")
               + " | " + fecha;
    }
}