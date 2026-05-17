package modelo;

public class Horario {

    private int id;
    private Medico medico;
    private String diaSemana;  // Lunes, Martes, etc.
    private String turno;      // Mañana, Tarde, Noche
    private boolean disponible;

    public Horario() {
    }

    public Horario(int id, Medico medico, String diaSemana,
                   String turno, boolean disponible) {
        this.id = id;
        this.medico = medico;
        this.diaSemana = diaSemana;
        this.turno = turno;
        this.disponible = disponible;
    }

    public void bloquear() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return (medico != null ? medico.getNombreCompleto() : "—")
               + " | " + diaSemana + " | " + turno;
    }
}