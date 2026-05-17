package modelo;

public class Medico extends Persona {

    private String cmp;        // Colegio Médico del Perú
    private String telefono;
    private String correo;
    private String turno;      // Mañana, Tarde, Noche
    private boolean disponible;
    private Especialidad especialidad;

    public Medico() {
    }

    public Medico(int id, String nombre, String apellido, String dni,
                  String cmp, String telefono, String correo,
                  String turno, boolean disponible, Especialidad especialidad) {
        super(id, nombre, apellido, dni);
        this.cmp = cmp;
        this.telefono = telefono;
        this.correo = correo;
        this.turno = turno;
        this.disponible = disponible;
        this.especialidad = especialidad;
    }

    // Polimorfismo: sobreescribe el método abstracto de Persona
    @Override
    public void mostrarDatos() {
        System.out.println("=== DATOS DEL MÉDICO ===");
        System.out.println("ID           : " + id);
        System.out.println("Nombre       : " + getNombreCompleto());
        System.out.println("DNI          : " + dni);
        System.out.println("CMP          : " + cmp);
        System.out.println("Especialidad : " + (especialidad != null ? especialidad.getNombre() : "No asignada"));
        System.out.println("Turno        : " + turno);
        System.out.println("Teléfono     : " + telefono);
        System.out.println("Disponible   : " + (disponible ? "Sí" : "No"));
    }

    // Getters y Setters
    public String getCmp() {
        return cmp;
    }

    public void setCmp(String cmp) {
        this.cmp = cmp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " — " +
               (especialidad != null ? especialidad.getNombre() : "Sin especialidad");
    }
}